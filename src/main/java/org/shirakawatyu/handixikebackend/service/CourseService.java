package org.shirakawatyu.handixikebackend.service;

import org.shirakawatyu.handixikebackend.common.Result;


/**
 * @author ShirakawaTyu
 */
public interface CourseService {

    // 不做处理返回所有课程的原值
    Result course(String no);

    Result courseCurWeek(String no);

    Result courseSelectedWeek(String no, int selectedWeek);

    Result useLocalCourse(int selectedWeek, String courseData);

    Result manualDeleteCache(String no);
}
