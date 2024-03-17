package org.shirakawatyu.handixikebackend.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.api.ScoreApi;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.exception.NotLoginException;
import org.shirakawatyu.handixikebackend.pojo.GradePointAverage;
import org.shirakawatyu.handixikebackend.pojo.Score;
import org.shirakawatyu.handixikebackend.service.ScoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Resource(name = "MatrixScoreApi")
    ScoreApi scoreApi;

    @Override
    public Result getScore(HttpSession session) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        LinkedHashMap<String, ArrayList<Score>> score = scoreApi.getScore(cookieStore);
        if (score == null) {
            throw new NotLoginException();
        }
        return Result.ok().data(JSON.toJSONString(score));
    }

    @Override
    public Result getGPA(HttpSession session) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        GradePointAverage gradePointAverage = scoreApi.getGradePointAverage(cookieStore);
        if (gradePointAverage != null) {
            JSONObject result = new JSONObject();
            result.put("gpa", gradePointAverage);
            return Result.ok().data(JSON.toJSONString(result));
        } else {
            throw new NotLoginException();
        }
    }

}