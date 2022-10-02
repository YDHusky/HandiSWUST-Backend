package org.shirakawatyu.handixikebackend;

import org.junit.jupiter.api.Test;
import org.shirakawatyu.handixikebackend.common.Const;
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
        double weeks = (System.currentTimeMillis() - Const.START_DATE) / (1000.0 * 60 * 60 * 24 * 7);
        System.out.println(weeks);
    }

}
