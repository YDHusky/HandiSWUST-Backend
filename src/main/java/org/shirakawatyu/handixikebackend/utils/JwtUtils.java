package org.shirakawatyu.handixikebackend.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import lombok.experimental.UtilityClass;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ShirakawaTyu
 */
@UtilityClass
public class JwtUtils {
    private static String signature;

    public static boolean verify(String token) {
        return JWTUtil.verify(token, signature.getBytes());
    }

    public static String create(Map<String, Object> payload) {
        Object cookieStore = payload.get("cookieStore");
        if (cookieStore instanceof BasicCookieStore basicCookieStore) {
            payload.put("cookieStore", JSON.toJSONString(basicCookieStore.getCookies(), JSONWriter.Feature.WriteClassName));
        }
        String payloadString = JSON.toJSONString(payload);
        return JWTUtil.createToken(Map.of("payload", payloadString), signature.getBytes());
    }

    public static Map<String, Object> getPayloads(JWT jwt) {
        String payload = (String) jwt.getPayloads().get("payload");
        @SuppressWarnings("unchecked")
        Map<String, Object> objectMap = (Map<String, Object>) JSON.parse(payload, JSONReader.Feature.SupportAutoType);
        Object cookieStore = objectMap.get("cookieStore");
        if (cookieStore instanceof String cookieStoreString) {
            JSONArray objects = JSON.parseArray(cookieStoreString, JSONReader.Feature.SupportAutoType);
            BasicCookieStore basicCookieStore = new BasicCookieStore();
            objects.forEach(o -> basicCookieStore.addCookie((Cookie) o));
            objectMap.put("cookieStore", basicCookieStore);
        }
        return objectMap;
    }

    @Component
    public static class InnerInjector {
        @Autowired
        public void setSignature(@Value("${jwt.signature}") String signature) {
            JwtUtils.signature = signature;
        }
    }
}
