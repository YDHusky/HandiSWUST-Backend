package org.shirakawatyu.handixikebackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
class HandiXikeBackendApplicationTests {

    @Autowired
    RestTemplate restTemplate;
    @Test
    void contextLoads() {
        List<String> cookies;
        ResponseEntity<String> entity = restTemplate.getForEntity("http://cas.swust.edu.cn/authserver/getKey", String.class);
        String text = entity.toString();
        Map<String, String> map = new HashMap<>();
        map.put("modulus", text.substring(17, text.indexOf("\",")));
        map.put("exponent", text.substring(text.indexOf("\"exponent\":\"")+12, text.indexOf("\"},")));
        cookies = entity.getHeaders().get("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<Objects> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> entity1 = restTemplate.exchange("http://cas.swust.edu.cn/authserver/captcha", HttpMethod.GET, httpEntity, byte[].class);
        byte[] bytes = entity1.getBody();
        System.out.println(cookies);
        System.out.println(Base64.getEncoder().encodeToString(bytes));
    }

}
