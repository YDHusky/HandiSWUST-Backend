package org.shirakawatyu.handixikebackend.controller;

import lombok.RequiredArgsConstructor;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 考试信息获取
 *
 * @author HuYuanYang
 * @date 2024/03/25
 */
@RestController
@RequiredArgsConstructor
public class ExamController {

    @Autowired
    private final ExamService examService;

    /**
     * 获取考试信息
     *
     * @param session 会期
     * @return {@code Result}
     */
    @RequestMapping("/api/v2/extension/getExam")
    public Result getExam() {
        return examService.getExam();

    }
}
