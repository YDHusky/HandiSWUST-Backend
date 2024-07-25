package org.shirakawatyu.handixikebackend.config;

import lombok.extern.slf4j.Slf4j;
import org.shirakawatyu.handixikebackend.aop.ApiLayerAspect;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.common.ResultCode;
import org.shirakawatyu.handixikebackend.exception.NotLoginException;
import org.shirakawatyu.handixikebackend.exception.OutOfCreditException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import java.net.SocketTimeoutException;

/**
 * @author ShirakawaTyu
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = SocketTimeoutException.class)
    public Result SocketTimeoutExceptionHandler(SocketTimeoutException ste) {
        StackTraceElement[] stackTrace = ste.getStackTrace();
        StringBuilder classLocation = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("ServiceImpl")) {
                classLocation.append(element.getClassName())
                        .append(": ")
                        .append(element.getLineNumber());
                break;
            }
        }
        log.warn("Timeout: " + classLocation);
        return Result.fail().code(ResultCode.TIMEOUT).msg("TIMEOUT");
    }

    @ExceptionHandler(value = HttpServerErrorException.InternalServerError.class)
    public Result HttpServerErrorExceptionHandler(HttpServerErrorException e) {
        HttpHeaders responseHeaders = e.getResponseHeaders();
        if (responseHeaders != null && responseHeaders.getHost() != null) {
            log.warn("500 Internal Server Error: " + responseHeaders.getHost().getHostName());
        }
        return Result.fail().code(ResultCode.REMOTE_SERVICE_ERROR).msg("REMOTE_SERVICE_ERROR");
    }

    @ExceptionHandler(value = ApiLayerAspect.CircuitBreakerException.class)
    public Result CircuitBreakerExceptionHandler() {
        return Result.fail().code(ResultCode.TIMEOUT).msg("TIMEOUT");
    }

    @ExceptionHandler(value = NotLoginException.class)
    public Result NotLoginExceptionHandler() {
        return Result.fail().code(ResultCode.LOGOUT).msg("LOGOUT");
    }

    @ExceptionHandler(value = OutOfCreditException.class)
    public Result OutOfCreditExceptionHandler() {
        return Result.fail().code(ResultCode.OUT_OF_CREDIT).msg("OUT_OF_CREDIT");
    }
}
