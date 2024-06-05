package com.example.app.config.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class CookieUtil {

    //Cookie에서 토큰 정보 꺼내오기
    public static String resolveToken(HttpServletRequest request) {

        if (request.getCookies() == null) {
            log.info("No cookies found in the request");
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtAuthorizationFilter.AUTHORIZATION_HEADER)).findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);
    }
}
