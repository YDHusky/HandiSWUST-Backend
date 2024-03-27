package org.shirakawatyu.handixikebackend.api;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.pojo.Lesson;

import java.util.List;

public interface CourseApi {
    List<Lesson> getCourse(CookieStore cookieStore);
}
