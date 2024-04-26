package org.shirakawatyu.handixikebackend.api.impl;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.jsoup.Jsoup;
import org.shirakawatyu.handixikebackend.api.ScoreApi;
import org.shirakawatyu.handixikebackend.exception.OutOfCreditException;
import org.shirakawatyu.handixikebackend.pojo.GradePointAverage;
import org.shirakawatyu.handixikebackend.pojo.Score;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.shirakawatyu.handixikebackend.utils.ScoreUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ShirakawaTyu
 */
@Component("MatrixScoreApi")
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
            if ("0".equals(list.get(0).split(" ")[0])) {
                return new GradePointAverage(0, 0);
            }
            String[] s = list.get(1).split(" ");
            allGPA = s[0].replace("平均绩点", "");
            requiredGPA = s[1].replace("必修课绩点", "");
            return new GradePointAverage(Double.parseDouble(allGPA), Double.parseDouble(requiredGPA));
        } catch (Exception e) {
            Logger.getLogger("MatrixScoreApi.getGradePointAverage => ").log(Level.SEVERE, resp);
            throw e;
        }
    }

    @Override
    public LinkedHashMap<String, ArrayList<Score>> getScore(CookieStore cookieStore) {
        List<String> scores = null;
        String resp = "";
        try {
            String info = Requests.postForString(
                    "http://cas.swust.edu.cn/authserver/login?service=" + SCORE_URL + "?event=studentPortal:DEFAULT_EVENT",
                    new LinkedMultiValueMap<>(), cookieStore);
            if (info.contains("账号禁止使用")) {
                throw new OutOfCreditException();
            }
            resp = Requests.getForString(SCORE_URL + "?event=studentProfile:courseMark", "", cookieStore);
            scores = Jsoup.parse(resp).getElementsByClass("UItable").select("tr").eachText();
            if (scores.isEmpty()) {
                return null;
            }
            return processScore(scores);
        } catch (Exception e) {
            if (scores != null) {
                Logger.getLogger("MatrixScoreApi.getScore => ").log(Level.SEVERE, resp);
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
        if (hashMap.isEmpty()) {
            return null;
        }
        if (!scores.isEmpty()) {
            Logger.getLogger("MatrixScoreApi.processScore => ").log(Level.WARNING, scores.toString());
        }
        return hashMap;
    }
}
