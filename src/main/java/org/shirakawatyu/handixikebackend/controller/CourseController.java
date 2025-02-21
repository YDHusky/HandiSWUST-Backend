package org.shirakawatyu.handixikebackend.controller;

import lombok.RequiredArgsConstructor;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.CourseService;
import org.springframework.web.bind.annotation.*;

/**
 * 获取课程信息
 *
 * @author HuYuanYang
 * @date 2024/03/25
 */
@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // no为学号，用于存取缓存的Key

    /**
     * 删除课程缓存
     *
     * @return {@code Result}
     */
    @PostMapping("/api/v2/course/clear")
    public Result deleteCache(@SessionAttribute String no) {
        return courseService.manualDeleteCache(no);
    }

    /**
     * 实验课程
     *
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/exp")
    public Result experimentCourse(@SessionAttribute String no) {
        return courseService.experimentCourse(no);
    }

    /**
     * 正常课程
     *
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/norm")
    public Result normalCourse(@SessionAttribute String no) {
        return courseService.normalCourse(no);
    }

}
