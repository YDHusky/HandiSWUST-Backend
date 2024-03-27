package org.shirakawatyu.handixikebackend.cache;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.pojo.Lesson;

import java.util.List;

/**
 * @author ShirakawaTyu
 */
public interface RawCourseCache {
    List<Lesson> getRawCourse(CookieStore cookieStore, String no);

    void deleteCache();

    boolean manualDeleteCache(String no);
}
