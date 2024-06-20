package com.example.app.config.auth.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{
    private String id;
    private Map<String,Object> attributes;

    @Override
    public String getUserId() {
//        return this.id;
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("nickname");
    }

    @Override
    public String getPhone() {
        return (String)attributes.get("phone_number");
    }

    @Override
    public String getSnsType() {
        return "kakao";
    }

    @Override
    public String getSnsId(){
        return this.id;
    }

    @Override
    public String getUserImg() {
        return (String)attributes.get("profile_image");
    }

    @Override
    public String getSnsConnectDate() {
        return (String)attributes.get("connected_at");
    }
}
