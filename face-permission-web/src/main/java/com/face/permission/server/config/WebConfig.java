package com.face.permission.server.config;

import com.face.permission.server.handler.LoginInterceptorHandler;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-08 09:34
 */
@Configuration
@ComponentScan(basePackages = "com.face.permission")
@EnableAspectJAutoProxy
@ServletComponentScan(basePackages = "com.face.permission.server.handler.filters")
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器使用LoginInterceptorHandler   /** 表示对所有请求进行拦截
        registry.addInterceptor(loginInterceptorHandler()).addPathPatterns("/**");
    }


    @Bean
    public LoginInterceptorHandler loginInterceptorHandler(){
        return new LoginInterceptorHandler();
    }
}
