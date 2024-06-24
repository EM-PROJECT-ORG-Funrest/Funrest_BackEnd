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
        // 아이디가 존재하는지 여부 반환
        ResponseEntity< Map<String, String>> responseEntity =   userService.findIdCheck(userId);
        return responseEntity;
    }


    @GetMapping("/member/findPw")
    public void findPw(){
        log.info("GET /th/member/findPw....");
    }

    @PostMapping("/member/findPwCheck")
    public ResponseEntity< Map<String, String>> finPwCheck(UserDto userDto,HttpSession session){
        // findPw에서 입력한 userId 가져오기
        String userId = userDto.getUserId();
        //랜덤 값 가져오기
        String randomValue = userService.randomValue();

        // 세션에 randomValue, userId 저장 나중에 유효성 검사 및 pw변경 시 userId 사용
        session.setAttribute("userId", userId);
        session.setAttribute("randomValue", randomValue);
        try {
            //여기서 가입정보 확인 가입된 사용자인지 아닌지 확인함 randomValue 는 메일전송 시 필요해서 같이 보냄
            ResponseEntity< Map<String, String>> responseEntity =   userService.findIdCheck2(userId, randomValue);
            return responseEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/member/findPwUrl/{randomValue}")
    public String findPwUrl(HttpServletRequest request, @PathVariable String randomValue) {
        // 세션으로 randomValue 가져오기 유효성 검사를 위해
        HttpSession session = request.getSession();
        String storedRandomValue = (String) session.getAttribute("randomValue");

        // 여기서 검사 url 의 randomValue와 저장된 randomValue 가 같은지 다른지 검사
        if (!randomValue.equals(storedRandomValue)) {
            // 다르면 error 페이지로 전송
            return "th/member/error.html";
        } else {
            // 같으면 findPwUrl 페이지로 전송
            return "/th/member/findPwUrl";
        }
    }

    @GetMapping("/member/findPwUrl")
    public void findPwUrl (){}




    @PostMapping("/member/findSetPw")
    public String findSetPw (HttpServletRequest request, UserDto userDto){
        // pw값 변경 시 유저 정보 확인을 위한 세션에 userId 가져오기
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        // fundOwUrl에서 전송한 pw 값 을 가져옴
        String userPw = userDto.getUserPw();
        // 암호화 해주고
        String userPwEncode = passwordEncoder.encode(userPw);

        // 비밀번호 재설정
        userService.findById(userId, userPwEncode);
        
        // 모든 로직 종료되었으니 세션값 삭제해주기
        session.removeAttribute("userId");
        session.removeAttribute("randomValue");
        
        //로그인 페이지로 리다이렉트 해주기
        return "redirect:/th/member/login";
    }
}