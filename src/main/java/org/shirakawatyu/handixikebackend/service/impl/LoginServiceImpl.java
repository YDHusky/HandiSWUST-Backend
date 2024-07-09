package org.shirakawatyu.handixikebackend.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.shirakawatyu.handixikebackend.api.LoginApi;
import org.shirakawatyu.handixikebackend.api.impl.CasLoginApi;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.service.LoginService;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;


/**
 * @author ShirakawaTyu
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Resource(name = "CasLoginApi")
    LoginApi casLoginApi;

    private final StringRedisTemplate redisTemplate;
    private final HttpSession session;

    @Override
    public Result getKey() {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        if (cookieStore == null) {
            return null;
        }
        Map<String, String> key = casLoginApi.getKey(cookieStore);
        return Result.ok().data(key);
    }

    @Override
    public Result getCaptcha() {
        BasicCookieStore cookieStore = new BasicCookieStore();
        session.setAttribute("cookieStore", cookieStore);
        byte[] captcha = casLoginApi.getCaptcha(cookieStore);
        return Result.ok().data(Base64.getEncoder().encodeToString(captcha));
    }


    @Override
    public Result login(String username, String password, String captcha) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        if (cookieStore == null) {
            return null;
        }
        int result = casLoginApi.login(username, password, captcha, cookieStore);
        if (result == ResultCode.LOGIN_SUCCESS) {
            session.setAttribute("no", username);
            // 统计每日登录人次
            recordLoginAction(username);
            return Result.ok().code(ResultCode.LOGIN_SUCCESS).msg("LOGIN SUCCESS");
        } else if (result == ResultCode.REMOTE_SERVICE_ERROR) {
            removeSession();
            return Result.fail().code(ResultCode.REMOTE_SERVICE_ERROR).msg("REMOTE SERVICE ERROR");
        }
        removeSession();
        return Result.fail().code(ResultCode.LOGIN_FAIL).msg("LOGIN FAIL");
    }

    @Override
    public Result logout() {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        casLoginApi.logout(cookieStore);
        removeSession();
        return Result.ok().code(ResultCode.LOGOUT_SUCCESS).msg("LOGOUT SUCCESS");
    }

    @Override
    public Result loginCheck() {
        if (session.getAttribute("no") == null) {
            return Result.ok().code(ResultCode.LOGOUT).msg("LOGOUT");
        }
        return Result.ok().code(ResultCode.HAS_LOGIN).msg("LOGIN");
    }

    private void removeSession() {
        session.removeAttribute("no");
        session.removeAttribute("cookieStore");
        session.invalidate();
    }


    public static final String DYNAMIC_CODE_URL = "http://cas.swust.edu.cn/authserver/getDynamicCode";


    @Override
    public String getDynamicCode(String phone) {
        BasicCookieStore store = new BasicCookieStore();
        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.put("mobile", Collections.singletonList(phone));

        String entity = Requests.postForString(DYNAMIC_CODE_URL, data, store);
        Map<String, String> resp = JSON.parseObject(entity, new TypeReference<Map<String, String>>() {

        });
        if ("true".equals(resp.get("success"))) {
            log.info("验证码获取成功");
        }
        session.setAttribute("cookieStore", store);
        return resp.get("msg");
    }


    @Override
    public Result loginByPhone(String phone, String code) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        if (cookieStore == null) {
            return null;
        }
        int ret = doInnerLogin(phone, code, cookieStore);
        switch (ret) {
            case ResultCode.LOGIN_SUCCESS -> {
                session.setAttribute("no", phone);
                recordLoginAction(phone);
                return Result.ok().code(ResultCode.LOGIN_SUCCESS).msg("LOGIN SUCCESS");
            }
            case ResultCode.REMOTE_SERVICE_ERROR -> {
                return Result.fail().code(ResultCode.REMOTE_SERVICE_ERROR).msg("REMOTE SERVICE ERROR");
            }
        }
        removeSession();
        return Result.fail().code(ResultCode.LOGIN_FAIL).msg("LOGIN FAIL");
    }

    private void recordLoginAction(String number) {
        // 统计每日登录人次
        String format = DateUtil.today();
        Thread.startVirtualThread(() -> redisTemplate.opsForHash().increment("count", format, 1));
        // 登录人数
        Thread.startVirtualThread(() -> redisTemplate.opsForHyperLogLog().add("DAU:" + format, number));
    }

    private int doInnerLogin(String phone, String code, CookieStore cookieStore) {
        String body = Requests.getForString(CasLoginApi.LOGIN_URL, "", cookieStore);
        String execution = "e1s1";
        try {
            Document parse = Jsoup.parse(Objects.requireNonNull(body));
            Elements formCont = parse.getElementsByAttributeValue("name", "execution");
            execution = formCont.getFirst().attr("value");
        } catch (Exception e) {
            log.debug("就当无事发生");
        }

        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("execution", execution);
        form.add("_eventId", "submit");
        form.add("geolocation", "");
        form.add("username", phone);
        form.add("lm", "dynamicLogin");
        form.add("dynamicCode", code);
        form.add("submit", "");

        try {
            String entity = Requests.postForString(CasLoginApi.LOGIN_URL, form, cookieStore);
            if (entity.contains("<title>西南科技大学教务管理系统 - 学生门户</title>") || cookieStore.getCookies().size() >= 3) {
                return ResultCode.LOGIN_SUCCESS;
            }
        } catch (HttpClientErrorException e) {
            log.info("{} {}", "手机验证码登录" + phone + "\n", body);
            int status = e.getStatusCode().value();
            if (cookieStore.getCookies().size() >= 3) {
                log.info("手机验证码登录: 一站式大厅崩溃，但登录接口正常");
            } else if (status == 401) {
                return ResultCode.LOGIN_FAIL;
            } else {
                return ResultCode.REMOTE_SERVICE_ERROR;
            }
        }
        return ResultCode.LOGIN_FAIL;
    }

}
