package org.shirakawatyu.handixikebackend.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.config.interfaces.MatchableHandlerInterceptor;
import org.shirakawatyu.handixikebackend.utils.JwtUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeChatInterceptor implements MatchableHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getHeader("User-Agent").contains("MicroMessenger")){
            response.getWriter().write(JSON.toJSONString(Result.fail().code(ResultCode.LOGOUT).msg("WeChat")));
            return false;
        }
        return true;
    }

}
