package org.shirakawatyu.handixikebackend.api.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shirakawatyu.handixikebackend.api.CourseApi;
import org.shirakawatyu.handixikebackend.exception.NotLoginException;
import org.shirakawatyu.handixikebackend.exception.OutOfCreditException;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
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

    public List<Lesson> getCourseFromMatrix(CookieStore cookieStore) {
        String url = "https://matrix.dean.swust.edu.cn/acadmicManager/index.cfm";
        ArrayList<Lesson> lessons = new ArrayList<>();
        try {
            String info = Requests.postForString(
                    "http://cas.swust.edu.cn/authserver/login?service=" + url + "?event=studentPortal:DEFAULT_EVENT",
                    new LinkedMultiValueMap<>(), cookieStore);
            if (info.contains("账号禁止使用")) {
                throw new OutOfCreditException();
            }
            String resp = Requests.getForString(url + "?event=studentPortal:courseTable", "", cookieStore);
            Document document = Jsoup.parse(resp);
            Element uiCourseTable = document.getElementsByClass("UICourseTable").first();
            Elements trs = uiCourseTable.getElementsByTag("tr");
            for (int course = 1; course < trs.size(); course++) {
                Element tr = trs.get(course);
                Elements tds = tr.children();
                if (tds.size() == 9){
                    tds.removeFirst();
                }
                for (int weekday = 0; weekday < tds.size(); weekday++) {
                    Element td = tds.get(weekday);
                    Elements spans = td.getElementsByTag("span");
                    if (spans.isEmpty()) {
                        continue;
                    }
                    String week = spans.get(3).text().replaceAll("\\(.*?\\)", "");
                    int sectionEnd = (course + 1) * 2;
                    lessons.add(new Lesson(
                            "(看到这个说明查询走的是教务系统)",
                            spans.get(2).text(), // teacher
                            spans.get(4).text(), // classroom
                            week,
                            "",
                            spans.get(0).text(), // name
                            String.valueOf(sectionEnd), 
                            String.valueOf(weekday), // weekday
                            String.valueOf(course + 1), // section
                            "",
                            String.valueOf(sectionEnd - 1))); // sectionStart
                }
            }
        } catch (Exception e) {
            log.error("成绩获取失败, 错误如下: {}\n", e.getMessage());
            throw e;
        }
        return lessons;
    }
}
