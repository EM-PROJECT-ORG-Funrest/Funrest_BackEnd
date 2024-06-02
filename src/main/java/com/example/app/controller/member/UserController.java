package com.example.app.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class UserController {

    @GetMapping("/login")
    public void login() { log.info("GET /login.."); }

//    @PostMapping("/login")
//    public void login_post(Exception error) {
//        log.info("POST /login.." + error);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userId, @RequestParam String password) {
        return ResponseEntity.ok().body("Login successful");
    }
}
