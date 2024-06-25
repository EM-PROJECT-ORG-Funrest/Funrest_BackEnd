package com.example.app.controller.member;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.service.member.SignUpService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/th/member/signUp")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/mail/req/{id}")
    public ResponseEntity req(@PathVariable("id") String id, HttpSession session) {
        log.info("GET /signUp/mail/req.. id : " + id);
        session.setMaxInactiveInterval(5*60); //세션 타임아웃 5분
        try {
            signUpService.sendEmail(id, session);
            return new ResponseEntity(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(null, HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/mail/verifyCode/{inputCode}")
    public boolean verifyCode(@PathVariable("inputCode") String inputCode, HttpSession session) {
        log.info("GET /signUp/mail/verifyCode.. inputCode : " + inputCode);
        return session.getAttribute("code").toString().equals(inputCode);
    }

    @GetMapping("/join")
    public void join() { log.info("GET /join.."); }

    @PostMapping("/join")
    public ResponseEntity join_post(UserDto userDto) {
        log.info("POST /join");
        log.info("controller userDto : " + userDto);
        try {
            boolean isSignUpSuccess = signUpService.userJoin(userDto);
            log.info("isSignUpSuccess : "  + isSignUpSuccess);
            if(!isSignUpSuccess) {
                return new ResponseEntity("SignUp Failed - already exist account", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity("SignUp Successful", HttpStatus.OK);
        } catch (Exception e) {
            log.info("error : " + e);
            return new ResponseEntity("Internal Server Error", HttpStatus.BAD_GATEWAY);
        }
    }
}
