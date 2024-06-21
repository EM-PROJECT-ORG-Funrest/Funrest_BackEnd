package com.example.app.config.auth;

import com.example.app.config.auth.provider.KakaoUserInfo;
import com.example.app.config.auth.provider.NaverUserInfo;
import com.example.app.config.auth.provider.OAuth2UserInfo;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PrincipalDetailsOauth2Service extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("");

        // OAuth2User Info
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User : " + oAuth2User);

        //provider 선별하기
        String snsType = userRequest.getClientRegistration().getRegistrationId();
        log.info("loadUser request snsType : " + snsType );

        OAuth2UserInfo oAuth2UserInfo = null;
        String snsConnectDate = null;
        String phone = null;
        String email = null;

        //정보 꺼내오기
        if(snsType!=null && snsType.startsWith("kakao")) {
            String id = oAuth2User.getAttributes().get("id").toString();
            Map<String, Object> attributes = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
            oAuth2UserInfo = new KakaoUserInfo(id, attributes);
            log.info("kakaoUserInfo : " + oAuth2UserInfo);
            String username = oAuth2UserInfo.getName();



            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
            log.info("kakao account : " + kakaoAccount);
            email = (String) kakaoAccount.get("email");
            log.info("kakao email : " + email);
            phone = (String) kakaoAccount.get("phone_number");
            log.info("kakao phone : " + phone);

            snsConnectDate = (String) super.loadUser(userRequest).getAttributes().get("connected_at");

        } else if(snsType!=null && snsType.startsWith("naver")){
            Map<String, Object> resp = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            String id = (String)resp.get("id");
            oAuth2UserInfo = new NaverUserInfo(id,resp);

//            NaverUserInfo naverUserInfo = (NaverUserInfo)oAuth2User;
//            String snsConnectDate = naverUserInfo.getSnsConnectDate();
            snsConnectDate = oAuth2UserInfo.getSnsConnectDate();

            phone = oAuth2UserInfo.getPhone();
            email = oAuth2UserInfo.getUserId();

            System.out.println("naverUserInfo : " +oAuth2UserInfo);
            System.out.println("naverUserInfo snsType : " + oAuth2UserInfo.getSnsType());
        }

        UserDto userDto = null;
        String username = oAuth2UserInfo.getName();

        // DB에서 이미 가입한 회원인지 조회
        Optional<User> userOptional = userRepository.findByUserId(email);

        if(userOptional.isEmpty()) {
            // 가입하지 않은 회원이면
            User user = new User();
            user.setUserId(email);
//            user.setUserName(oAuth2UserInfo.getName());
            user.setUserName(username);
            user.setRole("ROLE_USER");
            user.setSnsType(oAuth2UserInfo.getSnsType());
            /*user.setSnsId(oAuth2UserInfo.getSnsId());*/
            user.setUserImg(oAuth2UserInfo.getUserImg());
//            user.setSnsConnectDate((String) super.loadUser(userRequest).getAttributes().get("connected_at"));
            user.setSnsConnectDate(snsConnectDate);
            user.setPhone(phone);
            userRepository.save(user);

            userDto = UserDto.EntityToUserDto(user);
        } else {
            // 기존 계정이 존재한다면
            User user = userOptional.get();
            userDto = UserDto.EntityToUserDto(user);

            if(!snsType.equals(userDto.getSnsType())) {
                throw new BadCredentialsException("이미 회원가입 이력이 존재합니다.");
            }
        }

        log.info("loadUser 조회 결과 UserDto : " + userDto);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        PrincipalDetails principalDetails = new PrincipalDetails(userDto);
        principalDetails.setAccessToken(accessToken);

        return principalDetails;
    }

}
