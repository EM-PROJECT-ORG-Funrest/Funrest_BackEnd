package com.example.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view 에서 접근한 경로
    // 윈도우인 경우
    // private String savePath = "file:///C:/springboot_img/"; // 실제 파일 저장 경로
    // 맥인 경우a
    private String savePath = "file:///Users/hongjaeseong/springboot_img/"; // ProMainImg 실제 파일 저장 경로
    private String savePath2 = "file:///Users/hongjaeseong/springboot_subImg/"; // ProSubImg 실제 파일 저장 경로

    // view 에서 접근한 경로를 spring 이 실제 파일 저장 경로에서 찾아준다
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath)
                .addResourceLocations(savePath2);
    }

    // prevent CORS Error
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 허용할 출처
                .allowedMethods("GET", "POST") // 허용할 HTTP method
                .allowCredentials(true) // 쿠키 인증 요청 허용
                .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}
