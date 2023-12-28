package com.likelion.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.news.auth.OAuth2UserInfo;
import com.likelion.news.dto.response.LoginResponse;
import com.likelion.news.entity.User;
import com.likelion.news.repository.UserRepository;
import com.likelion.news.utils.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Long uid = ((OAuth2UserInfo) authentication.getPrincipal()).getKakaoUid();

        User findUser = userRepository.findUserByKakaoUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("UID 값을 찾을 수 없습니다."));

        Date now = new Date();

        String accessToken = jwtTokenProvider.sign(findUser, now);
        String refreshToken = jwtTokenProvider.sign(findUser, now);

        response.addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
        response.addCookie(new Cookie("refresh-token", refreshToken));


        log.info("사용자 Access 토큰 : {} ", accessToken);

        LoginResponse resBody = LoginResponse.builder()
                .uid(findUser.getUid())
                .accessToken(accessToken)
                .build();


        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resBody));

        getRedirectStrategy().sendRedirect(request, response, String.format("/callback?token=%s", accessToken));
    }


}
