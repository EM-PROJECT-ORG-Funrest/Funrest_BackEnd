package com.example.app.config.auth.loginHandler;

import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.domain.entity.RefreshToken;
import com.example.app.domain.repository.redis.RefreshTokenRepository;
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
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public CustomLoginSuccessHandler(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository, String defaultTargetUrl) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLoginSuccessHandler's onAuthenticationSuccess authentication : " + authentication);

        String accessToken = jwtTokenProvider.createToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken();
        System.out.println("JWT TOKEN : " + accessToken);

        // 액세스 토큰 저장
        Cookie cookie = new Cookie(JwtAuthorizationFilter.AUTHORIZATION_HEADER, accessToken);
        cookie.setMaxAge(86400);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // HTTPS 사용 시
        log.info("Setting cookie with name: " + JwtAuthorizationFilter.AUTHORIZATION_HEADER + " and value: " + accessToken);
        response.addCookie(cookie);
        // 응답 헤더 확인
        response.getHeaderNames().forEach(headerName -> {
            log.info(headerName + ": " + response.getHeader(headerName)); //Set-Cookie 정보
        });

        // 리프레시 토큰 저장
        log.info("loginSuccessHandler refresh 토큰용 userId 확인 : " + authentication.getName());
        log.info("loginSuccessHandler refresh token 저장.. " + refreshToken);
        refreshTokenRepository.save(new RefreshToken(authentication.getName(), refreshToken, accessToken));

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
