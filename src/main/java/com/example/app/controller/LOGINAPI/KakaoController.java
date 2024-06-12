package com.example.app.controller.LOGINAPI;

import com.example.app.domain.service.LOGINAPI.KakaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
@Slf4j
//@RequestMapping("th/member/login")
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    //getcode
    @GetMapping("th/member/login/kakaoLogin")
    public ModelAndView kakaoLogin(){
        log.info("GET th/member/login/kakaoLogin...");

        String redirectUrl = kakaoService.KakaoLoginRedirectUrl();
        return new ModelAndView("redirect:"+redirectUrl);

    }

    //callback
    @GetMapping("th/member/login/kakaoCallback")
    public String kakaoCallback(@RequestParam("code")String code) {
        log.info("GET th/member/login/kakao/code..." + code);

        String accessToken =kakaoService.ExchangeCodeForToken(code);

        kakaoService.KakaoGetProfile(accessToken);

        return "redirect:kakao/profile?accessToken="+accessToken;
    }

    //카카오계정과 함께 로그아웃
    @GetMapping("th/member/login/kakaoLogout")
    public ModelAndView kakaoLogout(){
        log.info("GET /th/member/login/kakaoLogout...");
        String redirectUrlMain = kakaoService.KakaoLogoutRedirectUrl();
        return new ModelAndView("redirect:"+redirectUrlMain);
    }

    //profile
    @GetMapping("th/member/login/kakao/profile")
    public String kakaoGetProfile(@RequestParam("accessToken") String accessToken){
        log.info("GET th/member/login/kakao/profile...");

        kakaoService.KakaoGetProfile(accessToken);
        log.info(accessToken);

        return "th/main/main";
    }

}
