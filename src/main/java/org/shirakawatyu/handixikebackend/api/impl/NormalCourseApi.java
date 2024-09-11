package org.shirakawatyu.handixikebackend.api.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author ShirakawaTyu
 */
@Component("NormalCourseApi")
@Slf4j
public class NormalCourseApi implements CourseApi {
    private static final String baseUrl = "http://sjjx.swust.edu.cn";

    @Override
    public List<Lesson> getCourse(CookieStore cookieStore) {
        Requests.getForBytes(baseUrl + "/swust", "", cookieStore);
        Requests.getForBytes(baseUrl + "/aexp/stuIndex.jsp", baseUrl + "/aexp/stuLeft.jsp", cookieStore);
        Requests.getForBytes(baseUrl + "/teachn/teachnAction/index.action", baseUrl + "/aexp/stuLeft.jsp", cookieStore);

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
                log.warn("错误JSON字符串：" + body);
            } else {
                log.warn("非法登录，请通过正规途径登录");
                throw new NotLoginException("非法登录");
            }
            // 一般来说这个问题是由于登录过期引起的
            throw new NotLoginException();
        }
        return lessonsArray;
    }
}
