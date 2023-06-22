package com.example.project.config.oauth.provider;

// OAuth2.0 제공자들 마다 응답해주는 속성값이 달라서 공통으로 만듦
public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
