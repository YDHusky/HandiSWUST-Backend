package org.shirakawatyu.handixikebackend.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.shirakawatyu.handixikebackend.service.LibraryService;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getLibrary(List<String> cookies) {
        if (cookies==null)return "3401 LOGOUT";
        System.out.println(cookies);
        Map<String, String> map = new HashMap<>();
        for (String cookie : cookies) {
            String[] split = cookie.split(";");
            String[] splitF = split[0].split("=");
            map.put(splitF[0], splitF[1]);
        }


        try {
            Connection.Response execute = Jsoup.connect("http://cas.swust.edu.cn/authserver/login?service=http://202.115.162.45:8080/reader/hwthau.php").followRedirects(false).cookies(map).execute();
            List<String> locations = execute.headers("Location");
            Connection.Response execute1 = Jsoup.connect(locations.get(0)).followRedirects(false).cookies(map).execute();
            List<String> headers = execute1.headers("Set-Cookie");
            cookies.add(headers.get(0));
            String[] split = headers.get(0).split("=");
            map.put(split[0],split[1]);
//            Connection.Response execute2 = Jsoup.connect("http://202.115.162.45:8080/reader/book_lst.php").followRedirects(false).cookies(map).execute();
            Requests.get("http://202.115.162.45:8080/reader/book_lst.php", "", cookies, restTemplate);
            ResponseEntity<String> entity = Requests.get("http://202.115.162.45:8080/reader/book_lst.php", "", cookies, restTemplate);//请求两遍才能进去


            String body = entity.getBody();
            Document parse = Jsoup.parse(body);
            Elements tr = parse.getElementsByTag("tr");


            String text = tr.text();
            String[] s = text.split(" ");
            System.out.println(Arrays.toString(s));//9:书名  -4：地址 -6 应还日期 -7 借出日期 /~著


        } catch (IOException e) {

           e.printStackTrace();
        }


        return "s";


    }
}