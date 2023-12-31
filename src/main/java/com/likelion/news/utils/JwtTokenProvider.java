package com.likelion.news.utils;

import com.likelion.news.entity.User;
import com.likelion.news.entity.enums.UserType;
import com.likelion.news.exception.ExceptionMessages;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private String secretKey;


    private final Long ACCESS_TOKEN_EXPIRE_TIME;

    private final Long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtTokenProvider(Environment env) {
        secretKey = env.getProperty("jwt.secret");
        ACCESS_TOKEN_EXPIRE_TIME = Long.parseLong(env.getProperty("jwt.access-token-expire-time"));
        REFRESH_TOKEN_EXPIRE_TIME = Long.parseLong(env.getProperty("jwt.refresh-token-expire-time"));
    }

    /**
     * methodName : sign
     * Author : Minseok Kim
     * description : 엑세스 토큰을 생성하는 메서드
     *
     * @param : User user - 토큰을 생성하려는 유저 정보
     * @param : now : token을 생성하는 시간
     * @return : 엑세스 토큰 리턴
     */
    public String sign(User user, Date now) {
        Claims claims = Jwts.claims().setSubject(user.getName());
        claims.put("uid", user.getUid());
        claims.put("userRole", user.getUserType());


        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * methodName : verify
     * Author : minseok Kim
     * description : 엑세스 토큰의 값을 읽고 결과값을 반환하는 함수
     *
     * @param : String Token - 해독하려는 토큰
     * @return : 해독하려는 토큰의 해독 결과
     * @exception ExpiredJwtException 토큰이 만료되었을 떄
     * @exception JwtException 토큰을 읽는 중 오류가 있을 때
     */
    public TokenVerificationResult verify(String token) throws ExpiredJwtException, JwtException{
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return TokenVerificationResult.builder()
                    .uid(claims.getBody().get("uid").toString())
                    .userID(claims.getBody().getSubject())
                    .userRole(UserType.valueOf((String)claims.getBody().get("userRole")))
                    .build();

            // 토큰이 만료된 경우와, 순수 JWT 토큰 오류를 구별해 Exception을 Throw한다.
        }catch (ExpiredJwtException e){
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException(ExceptionMessages.INVALID_TOKEN.getMessage(), e);
        }
    }

    /**
     * methodName : refresh
     * Author : Minseok Kim
     * description : Refresh Token을 생성하는 메서드
     *
     * @param : Date now - Refresh Token을 생성할 시간
     * @return : Refresh Token
     */
    public String createRefresh(Date now) {
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getClaim(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }



    /**
     * @methodName getAuthentication
     * @author Minseok kim
     * @description token으로 부터 유저 정보를 추출해 Authentication 객체를 생성하는 메서드
     *
     * @param  uid Jwt Token에서 추출된 uid
     * @param userType Jwt Token에서 추출된 사용자의 권한
     * @return Spring Security Context에 저장할 Authentication 객체
     */

    public Authentication getAuthentication(String uid,  UserType userType){

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userType.name());

        return new UsernamePasswordAuthenticationToken(uid, "",List.of(simpleGrantedAuthority));
    }

    @Getter
    @Builder
    public static class TokenVerificationResult {
        private String uid;
        private String userID;
        private UserType userRole;
        private String message;
    }
}