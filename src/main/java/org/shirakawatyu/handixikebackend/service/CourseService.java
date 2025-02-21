package org.shirakawatyu.handixikebackend.service;

import org.shirakawatyu.handixikebackend.common.Result;


/**
 * @author ShirakawaTyu
 */
public interface CourseService {

    Result manualDeleteCache(String no);

    Result experimentCourse(String no);

    Result normalCourse(String no);
}
