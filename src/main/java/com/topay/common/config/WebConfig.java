package com.topay.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // TODO: LoginCheckInterceptor 구현 후, 주입 필요
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor();
    }

    // TODO: LoginUserArgumentResolver 구현 후, 주입 필요
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // resolvers.add();
    }

}
