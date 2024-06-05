package com.example.app.controller;

import com.example.app.config.auth.jwt.CookieUtil;
import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        String jwt = CookieUtil.resolveToken(request);
        System.out.println("GlobalControllerAdvice addAttributes jwt : " + jwt);
    }
}
