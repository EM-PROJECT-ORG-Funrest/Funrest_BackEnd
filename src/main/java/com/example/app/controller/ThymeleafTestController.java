package com.example.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/th")
public class ThymeleafTestController {

    // admin view page test
    @GetMapping("/admin/adminDashboard")
    public void test8(){
        log.info("/GET /th/admin/adminDashboard...");
    }
    @GetMapping("/admin/adminMember")
    public void test9(){
        log.info("/GET /th/admin/adminMember...");
    }
    @GetMapping("/admin/adminPro")
    public void test10(){
        log.info("/GET /th/admin/adminPro...");
    }
    @GetMapping("/admin/adminProManage")
    public void test11(){
        log.info("/GET /th/admin/adminProManage...");
    }

    // member view page test
    @GetMapping("/member/signUp")
    public void test15(){
        log.info("/GET /th/member/singUp...");
    }


    // payment view page test
    @GetMapping("/payment/applyRefund")
    public void test23(){}
    @GetMapping("/payment/applyRefund_2")
    public void test24(){}
    @GetMapping("/payment/payment")
    public void test25(){}
    @GetMapping("/payment/paymentDetail")
    public void test26(){}
    @GetMapping("/payment/paymentHistory")
    public void test27(){}

}
