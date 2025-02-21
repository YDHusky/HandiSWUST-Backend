package org.shirakawatyu.handixikebackend.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.cache.RawCourseCache;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.shirakawatyu.handixikebackend.service.CourseService;
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


    @Override
    public Result manualDeleteCache(String no) {
        boolean success = rawCourse.manualDeleteCache(no);
        if (success) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result experimentCourse(String no) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        List<Lesson> lessonList = rawCourse.getExperimentCourse(cookieStore, no);
        return Result.ok().data(lessonList);
    }

    @Override
    public Result normalCourse(String no) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        List<Lesson> lessonList = rawCourse.getNormalCourse(cookieStore, no);
        return Result.ok().data(lessonList);
    }
}
