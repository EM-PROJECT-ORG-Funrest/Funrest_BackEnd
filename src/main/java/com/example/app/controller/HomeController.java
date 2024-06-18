package com.example.app.controller;

import com.example.app.domain.service.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home(){
        log.info("GET /");

        return "redirect:th/main/main";  // main.html 로 리다이렉팅 (주소창에 'http://localhost:8080' 입력 시)
    }
}
