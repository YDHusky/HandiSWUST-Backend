package org.shirakawatyu.handixikebackend.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.config.interfaces.MatchableHandlerInterceptor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author ShirakawaTyu
 */
@Component
public class LoginInterceptor implements MatchableHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("no") != null) {
            return true;
        } else {
            response.getWriter().write(JSON.toJSONString(Result.fail().code(ResultCode.LOGOUT)));
            return false;
        }
    }

    @Override
    public List<String> excludes() {
        return Arrays.asList(
                "/api/v2/login/**",
                "/api/count",
                "/api/week",
                "/api/web/version",
                "/api/v2/course/local/**"
        );
    }
}
