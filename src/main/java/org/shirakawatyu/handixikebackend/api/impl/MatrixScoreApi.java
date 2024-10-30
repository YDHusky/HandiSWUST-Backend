package org.shirakawatyu.handixikebackend.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.shirakawatyu.handixikebackend.api.ScoreApi;
import org.shirakawatyu.handixikebackend.exception.NotLoginException;
import org.shirakawatyu.handixikebackend.exception.OutOfCreditException;
import org.shirakawatyu.handixikebackend.pojo.GradePointAverage;
import org.shirakawatyu.handixikebackend.pojo.Score;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.shirakawatyu.handixikebackend.utils.ScoreUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

/**
 * @author ShirakawaTyu
 */
@Component("MatrixScoreApi")
@Slf4j
public class MatrixScoreApi implements ScoreApi {

    private static final String SCORE_URL = "https://matrix.dean.swust.edu.cn/acadmicManager/index.cfm";

    @Override
    public GradePointAverage getGradePointAverage(CookieStore cookieStore) {
        String allGPA, requiredGPA, resp = "";
        try {
            String info = Requests.postForString(
                    "http://cas.swust.edu.cn/authserver/login?service=" + SCORE_URL + "?event=studentPortal:DEFAULT_EVENT",
                    new LinkedMultiValueMap<>(), cookieStore);
            if (info.contains("账号禁止使用")) {
                throw new OutOfCreditException();
            }
            resp = Requests.getForString(SCORE_URL + "?event=studentProfile:courseMark", "", cookieStore);
            List<String> list = Jsoup.parse(Objects.requireNonNull(resp)).getElementsByClass("boxNavigation").eachText();
            if (list.isEmpty()) {
                return null;
            }
            // 教务系统当前还没有你的成绩
            String text = list.get(0).replace(" ", "");
            if ("0总学分".equals(text) || "0".equals(text.split(" ")[0])) {
                return new GradePointAverage(0, 0);
            }
            String[] s = list.get(1).split(" ");
            allGPA = s[0].replace("平均绩点", "");
            requiredGPA = s[1].replace("必修课绩点", "");
            return new GradePointAverage(Double.parseDouble(allGPA), Double.parseDouble(requiredGPA));
        } catch (Exception e) {
            log.error("成绩获取失败, 响应如下: {}\n", resp);
            throw e;
        }
    }

    @Override
    public LinkedHashMap<String, ArrayList<Score>> getScore(CookieStore cookieStore) {
        List<String> scores = null;
        String resp = "";
        String info = Requests.postForString(
                "http://cas.swust.edu.cn/authserver/login?service=" + SCORE_URL + "?event=studentPortal:DEFAULT_EVENT",
                new LinkedMultiValueMap<>(), cookieStore);
        if (info.contains("账号禁止使用")) {
            throw new OutOfCreditException();
        }
        resp = Requests.getForString(SCORE_URL + "?event=studentProfile:courseMark", "", cookieStore);
        Document document = Jsoup.parse(resp);
        if (document.getElementById("blueBar") == null) {
            throw new NotLoginException();
        }
        try {
            scores = document.getElementsByClass("UItable").select("tr").eachText();
            return processScore(scores);
        } catch (Exception e) {
            if (scores != null) {
                log.error("成绩获取失败, 响应如下: {}\n", resp);
            }
            throw e;
        }
    }

    private LinkedHashMap<String, ArrayList<Score>> processScore(List<String> scoreStings) {
        Queue<String> scores = new LinkedList<>(scoreStings);
        LinkedHashMap<String, ArrayList<Score>> hashMap = new LinkedHashMap<>();
        ScoreUtils.requiredScoreFilter(scores, hashMap);
        ScoreUtils.optionalScoreFilter(scores, hashMap);
        ScoreUtils.cetScoreFilter(scores, hashMap);
        if (!scores.isEmpty()) {
            log.warn(scores.toString());
        }
        return hashMap;
    }
}
