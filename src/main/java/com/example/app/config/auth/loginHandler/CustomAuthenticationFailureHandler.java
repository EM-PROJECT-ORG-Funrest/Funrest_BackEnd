package com.example.app.config.auth.loginHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("로그인 실패 : " + exception);

        String errorMsg = null;
        if(exception instanceof BadCredentialsException) {
            errorMsg = "아이디/패스워드가 올바르지 않습니다.";
        } else if(exception instanceof InsufficientAuthenticationException) {
            errorMsg = "Invalid Secret Key";
        }

        errorMsg = URLEncoder.encode(errorMsg, "UTF-8");
        setDefaultFailureUrl("/th/member/login?error=true&exception=" + errorMsg);

        super.onAuthenticationFailure(request, response, exception);
    }
}
