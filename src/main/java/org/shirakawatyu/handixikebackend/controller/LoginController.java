package org.shirakawatyu.handixikebackend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.LoginService;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.shirakawatyu.handixikebackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

/**
 * 与认证相关的接口
 * @author ShirakawaTyu
 * @since 2022/10/1 17:40
 */

@RestController
@RequestMapping("/api/v2/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/key")
    public Result getKey(HttpSession session) {
        return loginService.getKey(session);
    }

    @GetMapping("/captcha")
    public Result getCaptcha(HttpSession session) {
        return loginService.getCaptcha(session);
    }

    @PostMapping("/login")
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("captcha") String captcha, HttpSession session, HttpServletResponse response) {
        Result result = loginService.login(username, password, captcha, session);
        HashMap<String, Object> map = new HashMap<>();
        session.getAttributeNames().asIterator().forEachRemaining(key -> map.put(key, session.getAttribute(key)));
        Cookie cookie = new Cookie("Token", jwtUtils.create(map));
        cookie.setPath("/");
        cookie.setMaxAge((int) ((DateUtil.getTomorrow() - System.currentTimeMillis()) / 1000));
        response.addCookie(cookie);
        return result;
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session, HttpServletResponse response) {
        Cookie cookie = new Cookie("Token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return loginService.logout(session);
    }

    @GetMapping("/loginCheck")
    public Result loginCheck(HttpSession session) {
        return loginService.loginCheck(session);
    }
}