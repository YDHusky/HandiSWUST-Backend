package org.shirakawatyu.handixikebackend.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.config.interfaces.MatchableHandlerInterceptor;
import org.shirakawatyu.handixikebackend.utils.JwtUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author ShirakawaTyu
 */
@Component
public class SessionInterceptor implements MatchableHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Token".equals(cookie.getName()) && JwtUtils.verify(cookie.getValue())) {
                    JWT jwt = JWTUtil.parseToken(cookie.getValue());
                    HttpSession session = request.getSession();
                    JwtUtils.getPayloads(jwt).forEach(session::setAttribute);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> includes() {
        return Collections.singletonList("/**");
    }
}
