package com.likelion.news.auth;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo extends OAuth2User {
    String getProfileImage();
    String getEmail();
    String getName();
    Long getKakaoUid();
}