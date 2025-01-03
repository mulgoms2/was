package com.example.was.auth.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderServiceTest {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final long validityInMilliseconds = 3600000; // 1시간

    private String jwts;

    @BeforeEach
    void createAccessToken() {
        jwts = Jwts.builder().signWith(secretKey).expiration(new Date()).compact();
        assertNotNull(jwts);
    }

    @Test
    void validateToken() {
        // 정상적인 토큰인지 검증
        // 토큰이 변조되었다면 정상적으로 토큰이 파싱되지 않는다?
        boolean isSigned = Jwts.parser().verifyWith(secretKey).build().isSigned(jwts);
        assertTrue(isSigned);

    }

    @Test
    void getUsernameFromToken() {
    }

    @Test
    void parseToken() {
    }
}