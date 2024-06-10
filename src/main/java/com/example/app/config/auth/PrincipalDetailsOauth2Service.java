package com.example.app.config.auth;

import com.example.app.config.auth.provider.KakaoUserInfo;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

        // 정보 꺼내오기
        String id = oAuth2User.getAttributes().get("id").toString();
        Map<String, Object> attributes = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(id, attributes);
        log.info("kakaoUserInfo : " + kakaoUserInfo);
        String username = kakaoUserInfo.getName();

        // DB에서 이미 가입한 회원인지 조회
        Optional<User> userOptional = userRepository.findByUserId(id);

        UserDto userDto = null;

        if(userOptional.isEmpty()) {
            // 가입하지 않은 회원이면
            User user = new User();
            user.setUserId(id);
            user.setUserName(kakaoUserInfo.getName());
            user.setRole("ROLE_USER");
            user.setSnsType(kakaoUserInfo.getSnsType());
            user.setUserImg(kakaoUserInfo.getUserImg());
            user.setSnsConnectDate((String) super.loadUser(userRequest).getAttributes().get("connected_at"));
            user.setPhone(kakaoUserInfo.getPhone());
            userRepository.save(user);

            userDto = UserDto.EntityToUserDto(user);
        } else {
            // 기존 계정이 존재한다면
            User user = userOptional.get();
            userDto = UserDto.EntityToUserDto(user);
        }

        log.info("loadUser 조회 결과 UserDto : " + userDto);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        PrincipalDetails principalDetails = new PrincipalDetails(userDto);
        principalDetails.setAccessToken(accessToken);

        return principalDetails;
    }

}
