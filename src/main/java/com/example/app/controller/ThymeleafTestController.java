package com.example.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/th")
public class ThymeleafTestController {

    // member view page test
    @GetMapping("/member/findId")
    public void test12(){
        log.info("/GET /th/member/findId...");
    }
    @GetMapping("/member/findPw")
    public void test13(){
        log.info("/GET /th/member/findPw...");
    }
    @GetMapping("/member/login")
    public void test14(){
        log.info("/GET /th/member/login...");
    }
    @GetMapping("/member/signUp")
    public void test15(){
        log.info("/GET /th/member/singUp...");
    }


//    // payment view page test
//    @GetMapping("/payment/applyRefund")
//    public void test23(){}
//    @GetMapping("/payment/applyRefund_2")
//    public void test24(){}
//    @GetMapping("/payment/payment")
//    public void test25(){}
//    @GetMapping("/payment/paymentDetail")
//    public void test26(){}
//    @GetMapping("/payment/paymentHistory")
//    public void test27(){}

}
