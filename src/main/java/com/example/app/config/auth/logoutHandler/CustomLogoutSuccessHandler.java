package com.example.app.config.auth.logoutHandler;

import com.example.app.config.auth.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${kakao.rest-api}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.kakao.logout.redirect.uri}")
    private String KAKAO_LOGOUT_REDIRECT_URI;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String snsType = principalDetails.getUserDto().getSnsType();
        log.info("snstype : "+snsType);

        if ("kakao".equals(snsType)) {
            String kakaoLogoutWith = "https://kauth.kakao.com/oauth/logout?client_id=" + KAKAO_CLIENT_ID + "&logout_redirect_uri=" + URLEncoder.encode(KAKAO_LOGOUT_REDIRECT_URI,"UTF-8");
            response.sendRedirect(kakaoLogoutWith);
            return;
        }else if("naver".equals(snsType)){
            response.sendRedirect("redirect:https://nid.naver.com/nidlogin.logout?returl=https://www.naver.com/");
            return;
        } else if(snsType!=null) {
            response.sendRedirect("/");
            return;
        }
        log.info("CustomLogoutSuccessHandler's onLogoutSuccess()");
    }
}
