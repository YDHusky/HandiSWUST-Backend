package org.shirakawatyu.handixikebackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShirakawaTyu
 */
@RestController
public class TestController {
    @GetMapping("/api/test/ping")
    public String test() {
        return "Pong!b";
    }
}
