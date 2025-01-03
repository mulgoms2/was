package com.example.was.auth.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenProviderService {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final long accessTokenExpireTime = 5000;
    private final long refreshTokenExpireTime = 360000;

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

    public boolean validateToken(String token) {
        try {
            Date expiration = parseToken(token).getPayload().getExpiration();

            if (expiration.before(new Date())) {
                return false;  // 만료된 토큰은 유효하지 않음
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
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
