package com.example.app.config.auth.logoutHandler;

import com.example.app.config.auth.PrincipalDetails;
import com.example.app.config.auth.jwt.JwtAuthorizationFilter;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //log.info("CustomLogoutHandler logout() authentication : " + authentication); // null

        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtAuthorizationFilter.AUTHORIZATION_HEADER)).findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);

        if(token != null) {
            authentication = jwtTokenProvider.getAuthentication(token);
        }

        //kakao logout
        assert authentication != null;
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = principalDetails.getAccessToken();
        String snsType = principalDetails.getUserDto().getSnsType();

        if(snsType == null) {
            return;
        }

        if(snsType.startsWith("kakao")){
            String url = "https://kapi.kakao.com/v1/user/logout";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer "+accessToken);

            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

            HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity(params,headers);

            RestTemplate rt = new RestTemplate();
            try {
                ResponseEntity<String> resp = rt.exchange(url, HttpMethod.POST, entity, String.class);

                if(resp.getStatusCode() == HttpStatus.OK){
                    System.out.println("Successfully logout from kakao");
                }else{
                    System.out.println("Failed to logout from kakao. Status code : "+resp.getStatusCode());
                }
            }catch (HttpClientErrorException ex){
                System.out.println("Failed to logout from kakao. Error : "+ex.getMessage());
            }catch (Exception ex){
                System.out.println("An error occurred while logout from kakao. Error : "+ ex.getMessage());
            }
        }
    }
}
