package org.shirakawatyu.handixikebackend.api;

import org.apache.hc.client5.http.cookie.CookieStore;

public interface ExamApi {
    String getExam(CookieStore cookieStore);
}
