package com.example.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/th/payment")
public class PayController {

    @GetMapping("/payment")
    public void main(){
        log.info("GET /th/payment/payment....");
    }
}
