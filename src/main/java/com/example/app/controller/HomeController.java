package com.example.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(){
        log.info("GET /");
        return "redirect:th/main/main";  // main.html 로 리다이렉팅 (주소창에 'http://localhost:8080' 입력 시)
    }
}
