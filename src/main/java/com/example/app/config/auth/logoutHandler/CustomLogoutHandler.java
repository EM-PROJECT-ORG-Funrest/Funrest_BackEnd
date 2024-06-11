package com.example.app.config.auth.logoutHandler;

import com.example.app.config.auth.PrincipalDetails;
import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.domain.entity.RefreshToken;
import com.example.app.domain.repository.redis.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository, CustomLogoutSuccessHandler customLogoutSuccessHandler, JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtAuthorizationFilter.AUTHORIZATION_HEADER)).findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);

        if(token != null) {
            Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByAccessToken(token);
            refreshTokenOptional.ifPresent(refreshToken -> refreshTokenRepository.deleteById(refreshToken.getId()));
            authentication = jwtTokenProvider.getAuthentication(token);
            log.info("CustomLogoutHandler's logout authentication : " + authentication);
        }

        try {
            customLogoutSuccessHandler.onLogoutSuccess(request,response,authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
