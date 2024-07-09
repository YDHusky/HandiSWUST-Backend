package org.shirakawatyu.handixikebackend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.LoginService;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.shirakawatyu.handixikebackend.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 与认证相关的接口
 *
 * @author ShirakawaTyu
 * @since 2022/10/1 17:40
 */

@RestController
@RequestMapping("/api/v2/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final HttpSession session;

    /**
     * 获取密钥
     *
     * @return {@code Result}
     */
    @GetMapping("/key")
    public Result getKey() {
        return loginService.getKey();
    }

    /**
     * 获取验证码
     *
     * @return {@code Result}
     */
    @GetMapping("/captcha")
    public Result getCaptcha() {
        return loginService.getCaptcha();
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     * @param response 响应
     * @return {@code Result}
     */
    @PostMapping("/login")
    public Result login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("captcha") String captcha,
            HttpServletResponse response
    ) {
        username = username.replace(" ", "");
        Result result = loginService.login(username, password, captcha);
        HashMap<String, Object> map = new HashMap<>();
        session.getAttributeNames().asIterator().forEachRemaining(key -> map.put(key, session.getAttribute(key)));
        Cookie cookie = new Cookie("Token", JwtUtils.create(map));
        cookie.setPath("/");
        cookie.setMaxAge((int) ((DateUtil.getTomorrow() - System.currentTimeMillis()) / 1000));
        response.addCookie(cookie);
        return result;
    }

    /**
     * 注销
     *
     * @param response 响应
     * @return {@code Result}
     */
    @GetMapping("/logout")
    public Result logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("Token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return loginService.logout();
    }

    /**
     * 登录检查
     *
     * @return {@code Result}
     */
    @GetMapping("/loginCheck")
    public Result loginCheck() {
        return loginService.loginCheck();
    }

    /**
     * 获取手机验证码
     *
     * @param phone 电话
     * @return {@code Result }
     *
     */
    @GetMapping("/dynamicCode")
    public Result getMobileCaptchaCode(@RequestParam String phone) {
        return Result.ok().setMsg(loginService.getDynamicCode(phone));
    }

    /**
     * 手机验证码登录
     *
     * @param phone 电话
     * @param code 密码
     * @return {@code Result }
     *
     */
    @PostMapping("/phone")
    public Result loginByPhone(@RequestParam String phone, @RequestParam String code) {
        return loginService.loginByPhone(phone, code);
    }

}