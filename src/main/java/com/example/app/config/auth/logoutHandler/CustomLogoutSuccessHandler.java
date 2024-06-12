package com.example.app.config.auth.logoutHandler;

import com.example.app.config.auth.PrincipalDetails;
import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.domain.entity.RefreshToken;
import com.example.app.domain.repository.redis.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.kakao.logout.redirect.uri}")
    private String KAKAO_LOGOUT_REDIRECT_URI;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //log.info("CustomLogoutSuccessHandler logout() authentication : " + authentication); // null

        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtAuthorizationFilter.AUTHORIZATION_HEADER)).findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);

        if(token != null) {
            Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByAccessToken(token);
            refreshTokenOptional.ifPresent(refreshToken -> refreshTokenRepository.deleteById(refreshToken.getId()));
            authentication = jwtTokenProvider.getAuthentication(token);
        }

        log.info("CustomLogoutSuccessHandler authentication.getPrincipal() : " + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String snsType = principalDetails.getUserDto().getSnsType();
        log.info("onLogoutSuccess snstype : "+snsType);

        if (snsType!=null && "kakao".equals(snsType)) {
            String kakaoLogoutWith = "https://kauth.kakao.com/oauth/logout?client_id=" + KAKAO_CLIENT_ID + "&logout_redirect_uri=" + KAKAO_LOGOUT_REDIRECT_URI;
            log.info("kakaoLogoutWith : " + kakaoLogoutWith);
            response.sendRedirect(kakaoLogoutWith);
        }else if(snsType!=null && "naver".equals(snsType)){
            String naverLogoutWith = "https://nid.naver.com/nidlogin.logout?returl=https://www.naver.com/";
            response.sendRedirect("redirect:" + naverLogoutWith);
        } else if(snsType==null) {
            response.sendRedirect("/");
        }
    }
}
