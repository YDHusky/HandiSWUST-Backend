package org.shirakawatyu.handixikebackend.service.impl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.api.LoginApi;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;


/**
 * @author ShirakawaTyu
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource(name = "CasLoginApi")
    LoginApi casLoginApi;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result getKey(HttpSession session) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        if (cookieStore == null) {
            return null;
        }
        Map<String, String> key = casLoginApi.getKey(cookieStore);
        return Result.ok().data(key);
    }

    @Override
    public Result getCaptcha(HttpSession session) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        session.setAttribute("cookieStore", cookieStore);
        byte[] captcha = casLoginApi.getCaptcha(cookieStore);
        return Result.ok().data(Base64.getEncoder().encodeToString(captcha));
    }


    @Override
    public Result login(String username, String password, String captcha, HttpSession session) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        if (cookieStore == null) {
            return null;
        }
        int result = casLoginApi.login(username, password, captcha, cookieStore);
        if (result == ResultCode.LOGIN_SUCCESS) {
            session.setAttribute("no", username);
            // 统计每日登录人次
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Thread.startVirtualThread(() -> redisTemplate.opsForHash().increment("count", format, 1));
            // 登录人数
            Thread.startVirtualThread(() -> redisTemplate.opsForHyperLogLog().add("DAU:" + format, username));
            return Result.ok().code(ResultCode.LOGIN_SUCCESS).msg("LOGIN SUCCESS");
        } else if (result == ResultCode.REMOTE_SERVICE_ERROR) {
            removeSession(session);
            return Result.fail().code(ResultCode.REMOTE_SERVICE_ERROR).msg("REMOTE SERVICE ERROR");
        }
        removeSession(session);
        return Result.fail().code(ResultCode.LOGIN_FAIL).msg("LOGIN FAIL");
    }

    @Override
    public Result logout(HttpSession session) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        casLoginApi.logout(cookieStore);
        removeSession(session);
        return Result.ok().code(ResultCode.LOGOUT_SUCCESS).msg("LOGOUT SUCCESS");
    }

    @Override
    public Result loginCheck(HttpSession session) {
        if (session.getAttribute("no") == null) {
            return Result.ok().code(ResultCode.LOGOUT).msg("LOGOUT");
        }
        return Result.ok().code(ResultCode.HAS_LOGIN).msg("LOGIN");
    }

    private void removeSession(HttpSession session) {
        session.removeAttribute("no");
        session.removeAttribute("cookieStore");
    }
}
