package com.example.app.config.auth.loginHandler;

import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class CustomLoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public CustomLoginSuccessHandler(JwtTokenProvider jwtTokenProvider, String defaultTargetUrl) {
        this.jwtTokenProvider = jwtTokenProvider;
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLoginSuccessHandler's onAuthenticationSuccess authentication : " + authentication);

        String jwt = jwtTokenProvider.createToken(authentication);
        System.out.println("JWT TOKEN : " + jwt);

        Cookie cookie = new Cookie(JwtAuthorizationFilter.AUTHORIZATION_HEADER, jwt);
        cookie.setMaxAge(3600); //1시간 후 만료
        cookie.setPath("/");
        log.info("Setting cookie with name: " + JwtAuthorizationFilter.AUTHORIZATION_HEADER + " and value: " + jwt);
        response.addCookie(cookie);

        // 응답 헤더 확인
        response.getHeaderNames().forEach(headerName -> {
            log.info(headerName + ": " + response.getHeader(headerName)); //Set-Cookie 정보
        });

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
