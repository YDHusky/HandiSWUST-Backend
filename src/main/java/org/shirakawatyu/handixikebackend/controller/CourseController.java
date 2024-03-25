package org.shirakawatyu.handixikebackend.controller;

import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 获取课程信息
 *
 * @author HuYuanYang
 * @date 2024/03/25
 */
@RestController
public class CourseController {

    @Autowired
    CourseService courseService;

    // no为学号，用于存取缓存的Key

    /**
     * 全部课程信息
     *
     * @param session 会期
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/all")
    public Result course(HttpSession session) {
        String no = (String) session.getAttribute("no");
        return courseService.course(session, no);
    }

    /**
     * 获取本周课程
     *
     * @param session 会期
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/cur")
    public Result curCourse(HttpSession session) {
        String no = (String) session.getAttribute("no");
        return courseService.courseCurWeek(session, no);
    }

    /**
     * 查询指定周课程
     *
     * @param selectedWeek 选定一周
     * @param session      会期
     * @return {@code Result}
     */
    @GetMapping("/api/v2/course/select/{selectedWeek}")
    public Result selectedCourse(@PathVariable int selectedWeek, HttpSession session) {
        String no = (String) session.getAttribute("no");
        return courseService.courseSelectedWeek(session, no, selectedWeek);

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
     * @param session 会期
     * @return {@code Result}
     */
    @PostMapping("/api/v2/course/clear")
    public Result deleteCache(HttpSession session) {
        String no = (String) session.getAttribute("no");
        return courseService.manualDeleteCache(no);
    }

}
