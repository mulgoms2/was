package com.example.was.security.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenProviderService {
    private final SecretKey secretKey = Jwts.SIG.HS256.key()
                                                      .build();

    @Value("${jwtToken.accessToken.expiration}")
    private long accessTokenExpireTime;
    @Value("${jwtToken.refreshToken.expiration}")
    private long refreshTokenExpireTime;

    public String createAccessToken(String username) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                   .subject(username)
                   .issuedAt(now) // 발급 일시ㅏ
                   .expiration(expirationTime)  // 만료 시간 설정
                   .signWith(secretKey)      // 서명에 비밀 키 사용
                   .compact();
    }

    // 토큰이 파싱될때 시크릿키 및 시간 유효성에 대한 검사가 일어남
    public boolean validateToken(String token) throws JwtException {
        parseToken(token);
        return true;
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getPayload()
                                .getSubject();
    }

    private Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token);
    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + refreshTokenExpireTime);

        return Jwts.builder()
                   .subject(username)
                   .issuedAt(now) // 발급 일시ㅏ
                   .expiration(expirationTime)  // 만료 시간 설정
                   .signWith(secretKey)      // 서명에 비밀 키 사용
                   .compact();
    }
}
