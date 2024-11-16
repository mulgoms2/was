package com.example.was.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider {
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HMAC-SHA 알고리즘을 위한 키 생성
    private final long validityInMilliseconds = 3600000; // 1시간

    public String createToken(String username) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now) // 발급 일시ㅏ
                .expiration(expirationTime)  // 만료 시간 설정
                .signWith(secretKey)      // 서명에 비밀 키 사용
                .compact();
    }
}
