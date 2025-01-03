package com.example.was.auth.controller;

import com.example.was.auth.service.JwtTokenProviderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtTokenProviderService jwtTokenProviderService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (jwtTokenProviderService.validateToken(refreshToken)) {
            String username = jwtTokenProviderService.getUsernameFromToken(refreshToken);
            String accessToken = jwtTokenProviderService.createAccessToken(username);

            return ResponseEntity.ok().body(new AccessTokenResponse(accessToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RefreshTokenExpiredError("token_expired", "refresh token has been expired"));
    }

    private record RefreshTokenExpiredError(String error, String message) {
    }

    private record AccessTokenResponse(@Valid @NotEmpty String accessToken) {
    }

}
