package com.example.was.security.controller;

import com.example.was.security.service.JwtTokenProviderService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtTokenProviderService jwtTokenProviderService;

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
        try {
            if (jwtTokenProviderService.validateToken(refreshToken)) {
                String username = jwtTokenProviderService.getUsernameFromToken(refreshToken);
                String accessToken = jwtTokenProviderService.createAccessToken(username);

                return ResponseEntity.ok()
                                     .body(new AccessTokenResponse(accessToken));
            }
        } catch (ExpiredJwtException expiredJwtException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new RefreshTokenExpiredError("token_expired", "refresh token has been expired"));
        }

        return ResponseEntity.internalServerError()
                             .build();
    }

    private record RefreshTokenExpiredError(String error, String message) {
    }

    private record AccessTokenResponse(@Valid @NotEmpty String accessToken) {
    }

}
