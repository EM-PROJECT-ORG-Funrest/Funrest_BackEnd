package com.example.app.config.auth.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo{

    private String id;
    private Map<String,Object> attributes;

    private String snsConnectDate;

    public NaverUserInfo(String id, Map<String, Object> resp) {
        this.id = id;
        this.attributes = resp;
        setSnsConnectDate();
    }

    @Override
    public String getUserId() {
        return (String)attributes.get("email");
    }

//    @Override
//    public String getSnsId() {
//        return (String)attributes.get("id");
//    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getPhone() {
        return (String)attributes.get("mobile");
    }

    @Override
    public String getSnsType() {
        return "naver";
    }

    @Override
    public String getUserImg() {
        return (String)attributes.get("profile_image");
    }

    @Override
    public String getSnsConnectDate() {
        return snsConnectDate;
//        LocalDateTime recentLoginDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        return recentLoginDateTime.format(formatter);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    private void setSnsConnectDate() {
        // 최초 로그인 시간이 비어 있는 경우에만 설정
        if (snsConnectDate == null) {
            LocalDateTime recentLoginDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            snsConnectDate = recentLoginDateTime.format(formatter);
        }
    }
}