package com.example.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/th")
public class ThymeleafTestController {
    // main view page test
    @GetMapping("/main/adminHeader")
    public void test1(){
        log.info("/GET /th/main/adminHeader...");
    }
    @GetMapping("/main/afterLoginHeader")
    public void test2(){
        log.info("/GET /th/main/afterLoginHeader...");
    }
    @GetMapping("/main/afterLoginProjectHeader")
    public void test3(){
        log.info("/GET /th/main/afterLoginProjectHeader...");
    }
    @GetMapping("/main/beforeLoginHeader")
    public void test4(){
        log.info("/GET /th/main/beforeLoginHeader...");
    }
    @GetMapping("/main/beforeLoginProjectHeader")
    public void test5(){
        log.info("/GET /th/main/beforeLoginProjectHeader...");
    }
    @GetMapping("/main/footer")
    public void test6(){
        log.info("/GET /th/main/footer...");
    }


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

    // myPage > buyer view page test
    @GetMapping("/myPage/buyer/buyer")
    public void test16(){}
    @GetMapping("/myPage/buyer/editPassword")
    public void test17(){}
    @GetMapping("/myPage/buyer/editProfile")
    public void test18(){}
    @GetMapping("/myPage/buyer/signOut")
    public void test19(){
        log.info("/GET /th/myPage/buyer/signOut...");
    }

//    // myPage > seller view page test
//    @GetMapping("/myPage/seller/projectCreate")
//    public void test20(){}
//    @GetMapping("/myPage/seller/seller")
//    public void test22(){}

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

//    // project view page test
//    @GetMapping("/project/project")
//    public void test28(){}
}
