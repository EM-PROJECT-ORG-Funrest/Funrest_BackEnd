package com.example.app.config.auth.loginHandler;

import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

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

        //JWT TOKEN 생성 / 헤더로 전달 -> 쿠키로 변경하기......................
        String jwt = jwtTokenProvider.createToken(authentication);
        System.out.println("JWT TOKEN : " + jwt);

        Cookie cookie = new Cookie(JwtAuthorizationFilter.AUTHORIZATION_HEADER, jwt);
        cookie.setMaxAge(3600); //1시간 후 만료
        cookie.setPath("/");
        log.info("Setting cookie with name: " + JwtAuthorizationFilter.AUTHORIZATION_HEADER + " and value: " + jwt);
        response.addCookie(cookie);

//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add(JwtAuthorizationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // 로그 추가: 응답 헤더 확인
        response.getHeaderNames().forEach(headerName -> {
            log.info(headerName + ": " + response.getHeader(headerName));
        });

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
