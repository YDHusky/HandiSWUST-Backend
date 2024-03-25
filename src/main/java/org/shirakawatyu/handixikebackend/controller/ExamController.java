package org.shirakawatyu.handixikebackend.controller;

import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.impl.ExamServiceImpl;
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
public class ExamController {

    @Autowired
    ExamServiceImpl examService;

    /**
     * 获取考试信息
     *
     * @param session 会期
     * @return {@code Result}
     */
    @RequestMapping("/api/v2/extension/getExam")
    public Result getExam(HttpSession session){
        return examService.getExam(session);

    }
}
