package org.shirakawatyu.handixikebackend.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.shirakawatyu.handixikebackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ShirakawaTyu
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Token".equals(cookie.getName()) && jwtUtils.verify(cookie.getValue())) {
                    JWT jwt = JWTUtil.parseToken(cookie.getValue());
                    jwtUtils.getPayloads(jwt).forEach((key, value) -> request.getSession().setAttribute(key, value));
                }
            }
        }
        return true;
    }
}
