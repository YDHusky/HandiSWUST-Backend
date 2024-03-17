package org.shirakawatyu.handixikebackend.api;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.pojo.GradePointAverage;
import org.shirakawatyu.handixikebackend.pojo.Score;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface ScoreApi {
    GradePointAverage getGradePointAverage(CookieStore cookieStore);
    LinkedHashMap<String, ArrayList<Score>> getScore(CookieStore cookieStore);
}
