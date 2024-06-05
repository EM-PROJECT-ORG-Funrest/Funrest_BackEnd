package com.example.app.config.auth.jwt;

import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter{

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private JwtTokenProvider tokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // JWT 토큰의 인증 정보를 현재 실행중인 SecurityContext에 저장하는 역할
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = CookieUtil.resolveToken(request);
        String requestURI = request.getRequestURI();

        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 저장 완료.. 인증 정보 : " + authentication.getName() + " uri : " + requestURI);
        } else {
            log.info("유효한 JWT 토큰이 없습니다. uri : " + requestURI);
        }

        filterChain.doFilter(request, response);
    }
}