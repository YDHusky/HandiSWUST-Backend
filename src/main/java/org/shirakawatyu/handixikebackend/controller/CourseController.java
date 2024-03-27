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
     * 全部课程信息
     *
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/all")
    public Result course(@SessionAttribute String no) {
        return courseService.course(no);
    }

    /**
     * 获取本周课程
     *
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/cur")
    public Result curCourse(@SessionAttribute String no) {
        return courseService.courseCurWeek(no);
    }

    /**
     * 查询指定周课程
     *
     * @param selectedWeek 选定一周
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/select/{selectedWeek}")
    public Result selectedCourse(@PathVariable int selectedWeek, @SessionAttribute String no) {
        return courseService.courseSelectedWeek(no, selectedWeek);

    }

    /**
     * 使用本地课程
     *
     * @param selectedWeek 选定一周
     * @param courseData   课程数据
     * @return {@code Result}
     */
    @PostMapping("/api/v2/course/local/{selectedWeek}")
    public Result useLocalCourse(@PathVariable int selectedWeek, @RequestBody String courseData) {
        return courseService.useLocalCourse(selectedWeek, courseData);
    }

    /**
     * 删除课程缓存
     *
     * @return {@code Result}
     */
    @PostMapping("/api/v2/course/clear")
    public Result deleteCache(@SessionAttribute String no) {
        return courseService.manualDeleteCache(no);
    }

}
