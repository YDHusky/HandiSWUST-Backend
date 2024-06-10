package org.shirakawatyu.handixikebackend.api.impl;

import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jsoup.Jsoup;
import org.shirakawatyu.handixikebackend.api.ExamApi;
import org.shirakawatyu.handixikebackend.exception.OutOfCreditException;
import org.shirakawatyu.handixikebackend.pojo.Exam;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Component("examApi")
public class ExamApiImpl implements ExamApi {
    private String setExamList(String[] strings) {

        ArrayList<Exam> exams = new ArrayList<>();
        HashMap<String, List<Exam>> map = new HashMap<>();
        int examNum = (strings.length - 8) / 9;
        System.out.println(examNum);
        for (int p = 0; p < examNum; p++) {
            int aid = 8 + p * 9;
            if ("".equals(strings[aid])) {
                ArrayList<Exam> re = processExam(strings, aid);
                map.put("补考(已考完的科目仍然显示的话，是教务系统的锅)", re);
                break;
            }
            Exam exam = new Exam(strings[aid], strings[1 + aid], strings[2 + aid], strings[3 + aid], strings[4 + aid],
                    strings[5 + aid], strings[6 + aid], strings[7 + aid], strings[8 + aid]);
            exams.add(exam);
        }
        map.put("期末考试", exams);
        return JSONObject.toJSONString(map);
    }

    private static ArrayList<Exam> processExam(String[] strings, int aid) {
        ArrayList<Exam> re = new ArrayList<>();
        int start = aid + 1;
        int reNum = (strings.length - 8 - start) / 9;
        for (int p2 = 0; p2 < reNum; p2++) {
            int aid2 = 8 + p2 * 9;
            Exam exam = new Exam(strings[start + aid2], strings[start + 1 + aid2], strings[start + 2 + aid2], strings[start + 3 + aid2], strings[start + 4 + aid2],
                    strings[start + 5 + aid2], strings[start + 6 + aid2], strings[start + 7 + aid2], strings[start + 8 + aid2]);
            re.add(exam);
        }
        return re;
    }


    @Override
    public String getExam(CookieStore cookieStore) {
        Logger log = Logger.getLogger("ExamApiImpl.getExam :  ");
        try {
            String authInfo = Requests.getForString("https://matrix.dean.swust.edu.cn/acadmicManager/index.cfm?event=studentPortal:DEFAULT_EVENT", "", cookieStore);
            if (authInfo.contains("账号禁止使用")) {
                throw new OutOfCreditException();
            }
            String body = Requests.getForString("https://matrix.dean.swust.edu.cn/acadmicManager/index.cfm?event=studentPortal:examTable", "", cookieStore);
            String info = Jsoup.parse(body).getElementsByTag("td").text();
            String[] s = info.split(" ");
            int errCNT = 0;
            if((s.length-8)%9!=0){
                int examNum = (s.length - 8) / 9;
                for (int p = 0; p < examNum; p++) {
                    int aid = 8 + p * 9;
                    if(s[aid+8].length()<5){//小于五就证明有多余空格
                        errCNT++;
                        s[aid+1] = s[aid+1]+"-"+s[aid+2];
                        for(int i = aid+2;i < s.length-1;i++){
                            s[i] = s[i+1];
                        }
                    }
                }
            }
            String [] ns = new String[s.length-errCNT];
            for (int i = 0;i < s.length-errCNT;i ++){
                ns[i] = s[i];
            }
            if (s.length < 9) {
                return "no data";
            }
            return setExamList(ns);
        } catch (Exception e) {
            log.log(Level.SEVERE, "可能是登录凭证过期了，八成不会出这个问题");
            throw e;
        }
    }
}
