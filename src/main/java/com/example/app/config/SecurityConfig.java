package com.example.app.config;

import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import com.example.app.config.auth.loginHandler.CustomLoginSuccessHandler;
import com.example.app.config.auth.loginHandler.Oauth2JwtLoginSuccessHandler;
import com.example.app.config.auth.logoutHandler.CustomLogoutHandler;
import com.example.app.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import com.example.app.domain.repository.redis.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private CorsFilter corsFilter;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf((config) -> { config.disable(); });

        http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/", "/th/main/main", "/th/member/signUp/**", "/th/member/login").permitAll()
                    .requestMatchers("/th/myPage/buyer/buyer").hasRole("USER")
                    .anyRequest().authenticated()
            )
            .formLogin((login) -> login
                    .permitAll()
                    .loginPage("/th/member/login")
                    .usernameParameter("userId") //식별자가 userId이므로 username -> userId로 변경
                    //.successHandler(new CustomLoginSuccessHandler(jwtTokenProvider))
                    .successHandler(new CustomLoginSuccessHandler(jwtTokenProvider, refreshTokenRepository, "/th/main/main"))
                    .failureHandler(new CustomAuthenticationFailureHandler())
            )
            .logout((logout) -> logout
                    .permitAll()
                    .logoutUrl("/logout")
                    .addLogoutHandler(customLogoutHandler)
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .deleteCookies("JSESSIONID", JwtAuthorizationFilter.AUTHORIZATION_HEADER)
                    .invalidateHttpSession(true)
            );

        //OAUTH2-CLIENT
        http.oauth2Login((oauth2) -> oauth2
                .loginPage("/th/member/login")
                .successHandler(new Oauth2JwtLoginSuccessHandler(jwtTokenProvider, refreshTokenRepository,"/th/main/main"))
            );


        //세션 비활성화
        http.sessionManagement(
                httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
        );

        http.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적자원에 대한 보안 설정 무시
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
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
//    public CustomLogoutHandler customLogoutHandler() {
//        return new CustomLogoutHandler(refreshTokenRepository, customLogoutSuccessHandler, jwtTokenProvider);
//    }
}
