package org.shirakawatyu.handixikebackend.controller;


import org.shirakawatyu.handixikebackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.*;
/**
 * @description: 与认证相关的接口
 * @author ShirakawaTyu
 * @date: 2022/10/1 17:40
 */

@Controller
public class LoginController {
    @Autowired
    LoginService loginService;

    @GetMapping("/key")
    @ResponseBody
    public Map<String,String> getKey(HttpSession session) {
        return loginService.getKey(session);
    }

    @GetMapping("/captcha")
    @ResponseBody
    public String getCaptcha(HttpSession session) {

        return loginService.getCaptcha(session);
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("captcha") String captcha, HttpSession session) {

        return loginService.login(username, password, captcha, session);
    }

    @GetMapping("/logout")
    @ResponseBody
    public String logout(HttpSession session) {

        return loginService.logout(session);
    }
}