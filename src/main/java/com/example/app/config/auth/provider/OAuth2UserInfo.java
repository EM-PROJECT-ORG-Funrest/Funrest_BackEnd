package com.example.app.config.auth.provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String getUserId();
    String getName();
    //    String getEmail();
    String getPhone();
    String getSnsType();

    String getSnsId();

    String getUserImg();
    String getSnsConnectDate();
    Map<String,Object> getAttributes();
}
