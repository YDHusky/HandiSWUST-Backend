package org.shirakawatyu.handixikebackend.service.impl;

import org.shirakawatyu.handixikebackend.service.CourseService;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String course(List<String> cookies) {
        Requests.get("http://202.115.175.175/swust", "", cookies, restTemplate);
        Requests.get("http://202.115.175.175/aexp/stuIndex.jsp", "http://202.115.175.175/aexp/stuLeft.jsp", cookies, restTemplate);
        Requests.get("http://202.115.175.175/teachn/teachnAction/index.action", "http://202.115.175.175/aexp/stuLeft.jsp", cookies, restTemplate);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("op", "getJwTimeTable");
        map.add("time", Long.toString(System.currentTimeMillis()));

        ResponseEntity<String> entity = Requests.post("http://202.115.175.175/teachn/stutool", cookies, map, restTemplate);
        // 转码，不然会乱码
        return new String(entity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
