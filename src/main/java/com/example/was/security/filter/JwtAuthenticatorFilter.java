package com.example.was.security.filter;

import com.example.was.user.service.UserService;
import com.example.was.security.service.JwtTokenProviderService;
import com.example.was.constants.ApiConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticatorFilter extends OncePerRequestFilter {
    private final JwtTokenProviderService jwtTokenProviderService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getBearerTokenFrom(request);

        if (token != null && !isWhiteList(request) && jwtTokenProviderService.validateToken(token)) {
            Authentication authentication = getAuthentication(token); // 인가 정보를 처리
            // 이 필터가 UserNameAuthentication 필터보다 먼저 처리되기 떄문에 해당 Authentication 객체에 들어있는 권한 정보를 토대로 security config에서 hasRole을 활용할 수 있다.
            SecurityContextHolder.getContext()
                                 .setAuthentication(authentication); // 스프링 시큐리티 컨텍스트에 authenticaion 객체를 저장하면 인가된 api 요청으로 처리된다.
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        String username = jwtTokenProviderService.getUsernameFromToken(token);
        UserDetails userDetails = userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    // "Authorization" 헤더에서 JWT 토큰을 추출
    private String getBearerTokenFrom(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 제거
        }
        return null;
    }

    private boolean isWhiteList(HttpServletRequest request) {
        return Arrays.stream(ApiConstant.WHITE_LIST_URL)
                     .anyMatch(whiteUri -> whiteUri.equals(request.getRequestURI()));
    }
}
