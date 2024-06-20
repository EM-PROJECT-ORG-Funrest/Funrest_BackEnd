package com.example.app.controller.member;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.service.member.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/th")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/member/login")
    public void login1(){
        log.info("/GET /th/member/login...");
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "exception", required = false) String exception,
                           Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/th/member/login";
    }


    @GetMapping("/member/findId")
    public void findId(){
        log.info("/GET /th/member/findId...");
    }

    @PostMapping("/member/findIdCheck")
    public ResponseEntity< Map<String, String>> findIdCheck(UserDto userDto){
        String userId = userDto.getUserId();
        ResponseEntity< Map<String, String>> responseEntity =   userService.findIdCheck(userId);
        return responseEntity;
    }


    @GetMapping("/member/findPw")
    public void findPw(){
        log.info("GET /th/member/findPw....");
    }

    @PostMapping("/member/findPwCheck")
    public ResponseEntity< Map<String, String>> finPwCheck(UserDto userDto,HttpSession session){
        String userId = userDto.getUserId();
        String randomValue = userService.randomValue();

        session.setAttribute("userId", userId);
        session.setAttribute("randomValue", randomValue);
        try {
            ResponseEntity< Map<String, String>> responseEntity =   userService.findIdCheck2(userId, randomValue);
            return responseEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/member/findPwUrl/{randomValue}")
    public String findPwUrl(HttpServletRequest request, @PathVariable String randomValue) {
        HttpSession session = request.getSession();
        String storedRandomValue = (String) session.getAttribute("randomValue");

        if (!randomValue.equals(storedRandomValue)) {
            return "th/member/error.html";
        } else {
            return "/th/member/findPwUrl";
        }
    }

    @GetMapping("/member/findPwUrl")
    public void findPwUrl (){}




    @PostMapping("/member/findSetPw")
    public String findSetPw (HttpServletRequest request, UserDto userDto){

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        String userPw = userDto.getUserPw();
        log.info("/member/findSetPw invoke..." + userId + userPw);
        String userPwEncode = passwordEncoder.encode(userPw);

        // 비밀번호 재설정
        userService.findById(userId, userPwEncode);

        session.removeAttribute("userId");
        session.removeAttribute("randomValue");
        return "redirect:/th/main/main";
    }
}