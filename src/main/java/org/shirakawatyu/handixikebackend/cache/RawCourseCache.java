package org.shirakawatyu.handixikebackend.cache;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author ShirakawaTyu
 */
public interface RawCourseCache {
    List<Lesson> getRawCourse(CookieStore cookieStore, String no);

    void deleteCache();

    boolean manualDeleteCache(String no);

    @Cacheable(value = "Course", key = "'e'+#p1", unless = "null == #result")
    List<Lesson> getExperimentCourse(CookieStore cookieStore, String no);

    @Cacheable(value = "Course", key = "'n'+#p1", unless = "null == #result")
    List<Lesson> getNormalCourse(CookieStore cookieStore, String no);
}
