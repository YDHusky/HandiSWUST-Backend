package org.shirakawatyu.handixikebackend.controller;

import org.shirakawatyu.handixikebackend.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShirakawaTyu
 */
@RestController
public class TestController {
    @GetMapping("/api/v2/test/ping")
    public Result test() {
        return Result.ok().msg("Pong!");
    }
}
