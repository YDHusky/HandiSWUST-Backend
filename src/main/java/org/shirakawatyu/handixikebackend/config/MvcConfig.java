package org.shirakawatyu.handixikebackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ShirakawaTyu
 */
@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
//    private final List<MatchableHandlerInterceptor> interceptors;

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .excludePathPatterns(sessionInterceptor.excludes())
                .addPathPatterns(sessionInterceptor.includes());
        registry.addInterceptor(timeInterceptor)
                .excludePathPatterns(timeInterceptor.excludes())
                .addPathPatterns(timeInterceptor.includes());
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(loginInterceptor.excludes())
                .addPathPatterns(loginInterceptor.includes());

//        Consumer<MatchableHandlerInterceptor> register = (i) -> registry.addInterceptor(i)
//                .excludePathPatterns(i.excludes())
//                .addPathPatterns(i.includes());
//
//        interceptors.forEach(register);
    }
}
