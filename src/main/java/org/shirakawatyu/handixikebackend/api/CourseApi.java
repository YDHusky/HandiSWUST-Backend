package org.shirakawatyu.handixikebackend.api;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface CourseApi {
    List<Lesson> getCourse(CookieStore cookieStore);
}
