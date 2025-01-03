package com.example.was.common.filters;

import com.example.was.auth.service.JwtTokenProviderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticatorFilter extends OncePerRequestFilter {
    private final JwtTokenProviderService jwtTokenProviderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getBearerTokenFrom(request);

        if (token != null && !token.isEmpty()) {
            if (jwtTokenProviderService.validateToken(token)) {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 스프링 시큐리티 컨텍스트에 authenticaion 객체를 저장하면 인가된 api 요청으로 처리된다.
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        String username = jwtTokenProviderService.getUsernameFromToken(token);
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }

    // "Authorization" 헤더에서 JWT 토큰을 추출
    private String getBearerTokenFrom(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 제거
        }
        return null;
    }
}
