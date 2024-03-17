package org.shirakawatyu.handixikebackend.service.impl;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.api.ExamApi;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.ExamService;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {

    @Resource(name = "examApi")
    ExamApi examApi;


    @Override
    public Result getExam(HttpSession session) {
        CookieStore cookieStore = (CookieStore) session.getAttribute("cookieStore");
        String exam = examApi.getExam(cookieStore);
        if(JSONUtil.isTypeJSON(exam)) {
            return Result.ok().data(exam);
        }
        return Result.fail().msg(exam);
    }
}
