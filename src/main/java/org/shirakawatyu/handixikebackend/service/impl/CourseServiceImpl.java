package org.shirakawatyu.handixikebackend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.cache.RawCourseCache;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.shirakawatyu.handixikebackend.service.CourseService;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.shirakawatyu.handixikebackend.utils.LessonUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ShirakawaTyu
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final RawCourseCache rawCourse;
    private final HttpSession session;

    // 不做处理返回所有课程的原值
    @Override
    public Result course(String no) {
        List<Lesson> lessonList = rawCourse.getRawCourse((CookieStore) session.getAttribute("cookieStore"), no);
        JSONArray lessonsArray = new JSONArray(lessonList);
        if (!lessonsArray.isEmpty()) {
            return Result.ok().data(lessonsArray.toJSONString());
        }
        return Result.fail();
    }

    @Cacheable(value = "Course", key = "'c'+#p0", unless = "#result.data eq '[]'")
    @Override
    public Result courseCurWeek(String no) {
        List<Lesson> lessonList = rawCourse.getRawCourse((CookieStore) session.getAttribute("cookieStore"), no);
        String s = LessonUtils.simpleSelectWeek(Integer.parseInt(DateUtil.curWeek()), lessonList);
        return Result.ok().data(s);
    }

    @Override
    @Cacheable(value = "Course", key = "'s'+#p1+'s'+#p0", unless = "#result.data eq '[]'")
    public Result courseSelectedWeek(String no, int selectedWeek) {
        List<Lesson> lessonList = rawCourse.getRawCourse((CookieStore) session.getAttribute("cookieStore"), no);
        String s = LessonUtils.simpleSelectWeek(selectedWeek, lessonList);
        return Result.ok().data(s);
    }

    @Override
    public Result useLocalCourse(int selectedWeek, String courseData) {
        return Result.ok().data(LessonUtils.simpleSelectWeek(selectedWeek, JSONArray.parseArray(courseData, Lesson.class)));
    }

    @Override
    public Result manualDeleteCache(String no) {
        boolean success = rawCourse.manualDeleteCache(no);
        if (success) {
            return Result.ok();
        }
        return Result.fail();
    }
}
