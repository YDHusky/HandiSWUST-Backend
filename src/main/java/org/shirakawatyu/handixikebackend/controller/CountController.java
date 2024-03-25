package org.shirakawatyu.handixikebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统计计数相关
 *
 */
@Controller
public class CountController {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 统计日活
     *
     * @param date 日期
     * @return {@code String}
     */
    @GetMapping("/api/count")
    @ResponseBody
    public String countDAU(@RequestParam("date") String date) {
        return Long.toString(redisTemplate.opsForHyperLogLog().size("DAU:" + date));
    }
}
