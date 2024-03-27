package org.shirakawatyu.handixikebackend.controller;

import lombok.RequiredArgsConstructor;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.ScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 与成绩相关的接口
 *
 * @author Alice-in-Oven
 * @since 2022/10/2 15:00
 */
@RestController
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * 分数信息
     *
     * @return {@code Result}
     */
    @GetMapping("/api/v2/extension/scores")
    @ResponseBody
    public Result scores() {
        return scoreService.getScore();
    }

    /**
     * 获取绩点
     *
     * @return {@code Result}
     */
    @GetMapping("/api/v2/extension/gpa")
    @ResponseBody
    public Result gpa() {
        return scoreService.getGPA();
    }
}
