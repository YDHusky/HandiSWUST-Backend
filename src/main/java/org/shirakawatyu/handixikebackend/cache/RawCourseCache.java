package org.shirakawatyu.handixikebackend.cache;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author ShirakawaTyu
 */
public interface RawCourseCache {
    List<Lesson> getRawCourse(CookieStore cookieStore, String no);
    void deleteCache();
    boolean manualDeleteCache(String no);
}
