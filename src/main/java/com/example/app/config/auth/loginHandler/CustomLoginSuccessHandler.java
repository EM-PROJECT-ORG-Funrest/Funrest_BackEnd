package com.example.app.config.auth.loginHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLoginSuccessHandler's onAuthenticationSuccess authentication : " + authentication);

        Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();

        collection.forEach(role -> {
            String strRole = role.getAuthority();
            log.info("ROLE : " + strRole);

            try {
                if(strRole.equals("ROLE_USER")){
                    response.sendRedirect("/th/main/main");
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
