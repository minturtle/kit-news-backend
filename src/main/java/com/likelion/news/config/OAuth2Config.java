package com.likelion.news.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class OAuth2Config {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;


    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    private final String KAKAO_AUTHORIZATION_URL = "https://kauth.kakao.com/oauth/authorize";

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Bean
    public ClientRegistrationRepository kakaoClientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.kakaoClientRegistration());
    }

    private ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
                .clientId(KAKAO_CLIENT_ID)
                .clientSecret(KAKAO_CLIENT_SECRET)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile_nickname", "profile_image")
                .redirectUri(KAKAO_REDIRECT_URL)
                .authorizationUri(KAKAO_AUTHORIZATION_URL)
                .tokenUri(KAKAO_TOKEN_URL)
                .userInfoUri(KAKAO_USER_INFO_URL)
                .userNameAttributeName("id")
                .clientName("Kakao")
                .build();
    }

}
