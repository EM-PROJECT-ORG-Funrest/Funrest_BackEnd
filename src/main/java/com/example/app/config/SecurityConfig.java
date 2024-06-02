package com.example.app.config;

import com.example.app.config.auth.PrincipalDetails;
import com.example.app.config.auth.PrincipalDetailsService;
import com.example.app.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import com.example.app.config.auth.loginHandler.CustomLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf((config) -> { config.disable(); });

        http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/css/**", "/js/**", "/th/main/main", "/th/member/signUp", "th/member/login").permitAll()
                    .requestMatchers("/th/myPage/buyer/buyer").hasRole("USER")
                    .anyRequest().permitAll()
                    //.anyRequest().athenticated() //여기 css, js 따로 빼고 이걸로 고치기
            )
            .formLogin((login) -> login
                    .permitAll()
                    .loginPage("/th/member/login")
                    .usernameParameter("userId") //식별자가 userId이므로 username -> userId로 변경
                    //.defaultSuccessUrl("/th/main/main", true)
                    .successHandler(new CustomLoginSuccessHandler())
                    .failureHandler(new CustomAuthenticationFailureHandler())
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//
//        userDetailsManager
//                .createUser(User
//                        .withUsername("dhr2102@naver.com")
//                        .password(passwordEncoder.encode("test123!@#"))
//                        .roles("USER")
//                        .build()
//                );
//
//        return userDetailsManager;
//    }
}
