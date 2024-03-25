package org.shirakawatyu.handixikebackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试信息
 *
 * @author ShirakawaTyu
 * @date 2024/03/25
 */
@RestController
public class TestController {
    /**
     * 测试
     *
     * @return {@code String}
     */
    @GetMapping("/api/test/ping")
    public String test() {
        return "Pong!b";
    }
}
