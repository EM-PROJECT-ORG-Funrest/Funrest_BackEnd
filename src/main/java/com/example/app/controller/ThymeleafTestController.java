package com.example.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@Slf4j
@RequestMapping("/th/api")
public class ThymeleafTestController {
    @GetMapping("/message")
    public String test1(){
        log.info("GET /th/api/message.....");
        return "th/api/message";
    }

    @GetMapping("/test")
    public String test2(){
        log.info("GET /th/api/test.....");
        return "th/api/test";
    }
}
