package org.shirakawatyu.handixikebackend.api.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.api.CourseApi;
import org.shirakawatyu.handixikebackend.exception.NotLoginException;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ShirakawaTyu
 */
@Component("NormalCourseApi")
public class NormalCourseApi implements CourseApi {
    private static final String baseUrl = "http://sjjx.swust.edu.cn";

    @Override
    public List<Lesson> getCourse(CookieStore cookieStore) {
        Requests.getForBytes(baseUrl + "/swust", "", cookieStore);

        // 构造表单并拿到一般课表
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("op", "getJwTimeTable");
        map.add("time", DateUtil.getCurFormatDate());
        String body = Requests.postForString(baseUrl + "/teachn/stutool", map, cookieStore);

        // 转码，不然会乱码
        List<Lesson> lessonsArray;
        try {
            lessonsArray = JSON.parseArray(body, Lesson.class);
        } catch (JSONException e) {
            if (!body.contains("非法登录，请通过正规途径登录")) {
                Logger.getLogger("At NormalCourseApi JSONException => ").log(Level.WARNING, "错误JSON字符串：" + body);
            }
            // 一般来说这个问题是由于登录过期引起的
            throw new NotLoginException();
        }
        return lessonsArray;
    }
}
