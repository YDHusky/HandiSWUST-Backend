package org.shirakawatyu.handixikebackend.cache;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.pojo.Lesson;

import java.util.List;

/**
 * @author ShirakawaTyu
 */
public interface RawCourseCache {

    void deleteCache();

    boolean manualDeleteCache(String no);

    List<Lesson> getExperimentCourse(CookieStore cookieStore, String no);

    List<Lesson> getNormalCourse(CookieStore cookieStore, String no);
}
