package com.example.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view 에서 접근한 경로
    // 윈도우의 경우
    // private String savePath = "file:///C:/springboot_img/"; // 실제 파일 저장 경로
    // 맥인 경우
    private String savePath = "file:/Users/hongjaeseong/springboot_img/"; // 실제 파일 저장 경로

    // view 에서 접근한 경로를 spring 이 실제 파일 저장 경로에서 찾아준다
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
}
