package org.shirakawatyu.handixikebackend.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.config.interfaces.MatchableHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class TimeInterceptor implements MatchableHandlerInterceptor {
    @Value(value = "${swust.close-time}")
    int closeTime;
    @Value(value = "${swust.start-time}")
    int startTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int hour = new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
        if (hour >= closeTime && hour <= startTime) {
            response.getWriter().write(JSON.toJSONString(Result.fail().code(ResultCode.SERVER_CLOSE).msg("SERVER_CLOSE")));
            return false;
        }
        return true;
    }

    @Override
    public List<String> excludes() {
        return Arrays.asList(
                "/api/v2/login/loginCheck",
                "/api/count",
                "/api/week",
                "/api/web/version",
                "/api/v2/course/local/**"
        );
    }
}
