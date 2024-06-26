package com.example.app.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/th/member")
public class MemberController {

    @GetMapping("/signUp")
    public void signUp() {
        log.info("GET /th/member/signUp...");
    }
}
