package org.shirakawatyu.handixikebackend.config;

import lombok.RequiredArgsConstructor;
import org.shirakawatyu.handixikebackend.config.interfaces.MatchableHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author ShirakawaTyu
 */
@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
    private final List<MatchableHandlerInterceptor> interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        Consumer<MatchableHandlerInterceptor> register = (i) -> registry.addInterceptor(i)
                .excludePathPatterns(i.excludes())
                .addPathPatterns(i.includes());

        interceptors.forEach(register);
    }
}
