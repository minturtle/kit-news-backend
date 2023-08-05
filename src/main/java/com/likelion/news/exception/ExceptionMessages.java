package com.likelion.news.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessages {

    INVALID_TOKEN("엑세스 토큰이 올바르지 않습니다."),
    INVALID_TOKEN_TYPE("엑세스 토큰 타입이 올바르지 않습니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다."),
    LOGIN_NEED("로그인이 필요합니다."),
    CANNOT_FIND_USER("유저 정보를 찾을 수 없습니다."),
    CANNOT_FIND_ENTITY("엔티티 정보를 찾을 수 없습니다."),
    CANNOT_FIND_ENUM("특정 카테고리를 조회할 수 없습니다"),
    LOGIN_FAILURE("아이디 또는 비밀번호가 잘못되었습니다.");
    private String message;


}
