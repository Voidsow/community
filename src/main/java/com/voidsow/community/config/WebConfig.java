package com.voidsow.community.config;

import com.voidsow.community.interceptor.CacheInterceptor;
import com.voidsow.community.interceptor.LoginInterceptor;
import com.voidsow.community.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    UserInterceptor userInterceptor;
    LoginInterceptor loginInterceptor;

    @Autowired
    public WebConfig(UserInterceptor userInterceptor, LoginInterceptor loginInterceptor) {
        this.userInterceptor = userInterceptor;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CacheInterceptor()).addPathPatterns("/captcha");
        registry.addInterceptor(userInterceptor).
                excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpeg", "/**/*.jpg");
        registry.addInterceptor(loginInterceptor).
                excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpeg", "/**/*.jpg");
    }
}