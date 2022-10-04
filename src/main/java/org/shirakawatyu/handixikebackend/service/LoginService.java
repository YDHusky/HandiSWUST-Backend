package org.shirakawatyu.handixikebackend.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface LoginService {
    Map<String,String> getKey(HttpSession session);
    String getCaptcha(HttpSession session);
    String login(String username, String password, String captcha, HttpSession session);
    String logout(HttpSession session);
}
