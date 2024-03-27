package org.shirakawatyu.handixikebackend.api.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.shirakawatyu.handixikebackend.api.LoginApi;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ShirakawaTyu
 */
@Component("CasLoginApi")
public class CasLoginApi implements LoginApi {
    private static final String KEY_URL = "http://cas.swust.edu.cn/authserver/getKey";
    private static final String CAPTCHA_URL = "http://cas.swust.edu.cn/authserver/captcha";
    private static final String LOGIN_URL = "http://cas.swust.edu.cn/authserver/login?service=https://matrix.dean.swust.edu.cn/acadmicManager/index.cfm?event=studentPortal:DEFAULT_EVENT";

    @Override
    public Map<String, String> getKey(CookieStore cookieStore) {
        String entity = Requests.getForString(KEY_URL, "", cookieStore);
        JSONObject key = JSON.parseObject(entity);
        Map<String, String> map = new HashMap<>();
        map.put("modulus", key.getString("modulus"));
        map.put("exponent", key.getString("exponent"));
        return map;
    }

    @Override
    public byte[] getCaptcha(CookieStore cookieStore) {
        return Requests.getForBytes(CAPTCHA_URL, "", cookieStore);
    }

    @Override
    public int login(String username, String password, String captcha, CookieStore cookieStore) {
        String body = Requests.getForString(LOGIN_URL, "", cookieStore);
        String execution;
        try {
            Document parse = Jsoup.parse(Objects.requireNonNull(body));
            Elements formCont = parse.getElementsByAttributeValue("name", "execution");
            execution = formCont.getFirst().attr("value");
        } catch (Exception e) {
            execution = "e1s1";
        }

        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("execution", execution);
        map.add("_eventId", "submit");
        map.add("geolocation", "");
        map.add("username", username);
        map.add("lm", "usernameLogin");
        map.add("password", password);
        map.add("captcha", captcha);

        try {
            String entity = Requests.postForString(LOGIN_URL, map, cookieStore);
            if (entity.contains("<title>西南科技大学教务管理系统 - 学生门户</title>") || cookieStore.getCookies().size() >= 3) {
                return ResultCode.LOGIN_SUCCESS;
            }
        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            if (cookieStore.getCookies().size() >= 3) {
                Logger.getLogger("CasLoginApi.login => ").log(Level.WARNING, "一站式大厅崩溃，但登录接口正常");
            } else if (status == 401) {
                return ResultCode.LOGIN_FAIL;
            } else {
                return ResultCode.REMOTE_SERVICE_ERROR;
            }
        }
        return ResultCode.LOGIN_FAIL;
    }

    @Override
    public boolean logout(CookieStore cookieStore) {
        return true;
    }
}
