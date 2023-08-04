package com.likelion.news.service;

import com.likelion.news.auth.KakaoUserInfo;
import com.likelion.news.auth.OAuth2UserInfo;
import com.likelion.news.entity.User;
import com.likelion.news.entity.enums.LoginType;
import com.likelion.news.entity.enums.UserType;
import com.likelion.news.repository.UserRepository;
import com.likelion.news.utils.NanoIdProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * methodName : loadUser
     * Author : Junha
     * description : - 인증된 OAuth2 사용자를 불러오는 메서드
     *
     * @param : OAuth2UserRequest userRequest
     * @return : 찾거나 저장한 유저 반환
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            String provider = userRequest.getClientRegistration().getRegistrationId();
            OAuth2UserInfo oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            findOrCreateUser(oAuth2UserInfo, provider);

            return oAuth2UserInfo;
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }


    /**
     * methodName : findOrCreateUser
     * Author : Junha
     * description : - 주어진 OAuth2 사용자 정보를 바탕으로 DB에서 사용자를 찾고, 없는 경우 새 사용자를 생성하는 메서드
     *
     * @param : OAuth2UserInfo oAuth2UserInfo, String provider
     * @return : 찾거나 생성한 User 객체 반환
     */
    private User findOrCreateUser(OAuth2UserInfo oAuth2UserInfo, String provider) {
        Optional<User> optionalUser = userRepository.findUserByKakaoUid(oAuth2UserInfo.getKakaoUid());
        LoginType loginType = LoginType.fromString(provider);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            User user = User.builder()
                    .name(oAuth2UserInfo.getName())
                    .profileImage(oAuth2UserInfo.getProfileImage())
                    .loginType(loginType)
                    .kakaoUid(oAuth2UserInfo.getKakaoUid())
                    .uid(NanoIdProvider.randomNanoId())
                    .userType(UserType.ROLE_USER)
                    .build();

            userRepository.save(user);
            return user;
        }

    }


}

