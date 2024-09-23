package org.shirakawatyu.handixikebackend.service;

import org.shirakawatyu.handixikebackend.common.Result;


/**
 * @author ShirakawaTyu
 */
public interface CourseService {
    @Deprecated
    // 不做处理返回所有课程的原值
    Result course(String no);
    @Deprecated
    Result courseCurWeek(String no);
    @Deprecated
    Result courseSelectedWeek(String no, int selectedWeek);
    @Deprecated
    Result useLocalCourse(int selectedWeek, String courseData);

    Result manualDeleteCache(String no);

    Result experimentCourse(String no);

    Result normalCourse(String no);
}
