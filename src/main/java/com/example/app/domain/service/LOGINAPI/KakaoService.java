package com.example.app.domain.service.LOGINAPI;

import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    private String CLIENT_ID="c4b54896a51bc9c782e2794ae7954da1";
    private String REDIRECT_URI="http://localhost:8080/th/main/main";

    //login
    public String KakaoLoginRedirectUrl(){
        return "https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code&prompt=select_account";
    }

    //callback
    public String ExchangeCodeForToken(String code){
        //URL
        String url="https://kauth.kakao.com/oauth/token";

        //HEADER
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //PARAM
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",CLIENT_ID);
        params.add("redirect_uri",REDIRECT_URI);
        params.add("code",code);

        //ENTITY
        HttpEntity< MultiValueMap<String,String> > entity = new HttpEntity(params,headers);

        //REQEUST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<KakaoAuthResponse> responseEntity = rt.postForEntity(url,entity,KakaoAuthResponse.class);
        KakaoAuthResponse kakaoAuthResponse = responseEntity.getBody();

        //RESPONSE
        return kakaoAuthResponse.getAccess_token();


    }

    //logout
    public String KakaoLogoutRedirectUrl(){
        return "https://kauth.kakao.com/oauth/logout"+"?client_id="+CLIENT_ID+"&logout_redirect_uri="+REDIRECT_URI;
    }

    public @ResponseBody void KakaoGetProfile(String accessToken){
        //URL
        String url = "https://kapi.kakao.com/v2/user/me";

        //HEADER
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //ENTITY
        HttpEntity entity = new HttpEntity(headers);

        //REQUEST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST,entity, String.class);

        //RESPONSE
        System.out.println(response.getBody());
    }


    @Data
    public class KakaoAuthResponse{
        public String access_token;
        public String token_type;
        public String refresh_token;
        public int expires_in;
        public String scope;
        public int refresh_token_expires_in;
    }

}
