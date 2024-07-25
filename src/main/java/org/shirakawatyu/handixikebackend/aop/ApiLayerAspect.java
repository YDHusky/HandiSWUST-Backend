package org.shirakawatyu.handixikebackend.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.shirakawatyu.handixikebackend.exception.RequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ShirakawaTyu
 */
@Aspect
@Slf4j
@Component
public class ApiLayerAspect {
    private final Map<String, Count> errorCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> breakTime = new ConcurrentHashMap<>();
    @Value("${swust.api.breaker.threshold:20}")
    private int THRESHOLD;    // 超时阈值，单位：次
    @Value("${swust.api.breaker.break-millisecond:120000}")
    private long BREAK_MILLISECOND;    // 熔断时间，单位：ms
    @Value("${swust.api.breaker.circle:60000}")
    private long CIRCLE;    // 统计周期，单位：ms

    @Pointcut("execution(* org.shirakawatyu.handixikebackend.api.impl.*.*(..))")
    public void exception() {
    }

    @Around("exception()")
    public Object around(ProceedingJoinPoint point) {
        String method = point.getSignature().toShortString();
        Count cnt = errorCounts.get(method);
        Long time = breakTime.get(method);
        if (time != null) {
            if (System.currentTimeMillis() - time < BREAK_MILLISECOND) {
                throw new CircuitBreakerException();
            } else {
                breakTime.remove(method);
            }
        }
        if (cnt == null) {
            cnt = new Count(0, System.currentTimeMillis());
            errorCounts.put(method, cnt);
        } else if (System.currentTimeMillis() > cnt.lastCountTime.get() + CIRCLE) {
            cnt.times.set(0);
            cnt.lastCountTime.set(System.currentTimeMillis());
        } else if (cnt.times.get() >= THRESHOLD) {
            breakTime.put(method, System.currentTimeMillis());
            cnt.times.set(0);
            cnt.lastCountTime.set(System.currentTimeMillis());
            log.warn("{} 请求错误次数过多，触发熔断 {}ms", method, BREAK_MILLISECOND);
            throw new CircuitBreakerException();
        }
        try {
            return point.proceed();
        } catch (Throwable e) {
            switch (e.getCause()) {
                case ResourceAccessException e1 -> {
                    Throwable rootCause = e1.getRootCause();
                    if (rootCause instanceof HttpServerErrorException.GatewayTimeout | rootCause instanceof SocketTimeoutException |
                            (rootCause instanceof HttpHostConnectException && rootCause.getMessage().contains("timed out")) |
                            rootCause instanceof ConnectionRequestTimeoutException) {
                        defaultHandler(cnt, method, e1);
                    } else {
                        throw new RuntimeException(e1);
                    }
                }
                case HttpServerErrorException.BadGateway e1 -> defaultHandler(cnt, method, e1);
                case IOException e1 -> defaultHandler(cnt, method, e1);
                case RequestException e1 -> defaultHandler(cnt, method, e1);
                default -> throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static class CircuitBreakerException extends RuntimeException {
    }

    static class Count {
        AtomicInteger times;
        AtomicLong lastCountTime;

        public Count(int times, long lastCountTime) {
            this.times = new AtomicInteger(times);
            this.lastCountTime = new AtomicLong(lastCountTime);
        }
    }

    private void defaultHandler(Count cnt, String method, Throwable e) {
        cnt.times.updateAndGet((x) -> {
            log.warn("{}: {} {}", e.getClass().getSimpleName(), method, cnt.times);
            return x + 1;
        });
        throw new CircuitBreakerException();
    }
}
