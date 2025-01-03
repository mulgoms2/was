package com.example.was.auth.controller;

import com.example.was.auth.service.AuthService;
import com.example.was.auth.service.JwtTokenProviderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProviderService jwtTokenProviderService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            UserDetails userDetails = authService.loadUserByUsername(loginRequest.email());

            // db에서 조회된 계정의 비밀번호와 로그인 시도시 보낸 비밀번호가 일치하는지 확인
            if (!passwordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String accessToken = jwtTokenProviderService.createAccessToken(loginRequest.email());
            String refreshToken = jwtTokenProviderService.createRefreshToken(loginRequest.email());

            return ResponseEntity.ok()
                    .body(new LoginResponse(accessToken, refreshToken));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private record LoginRequest(@NotEmpty @Email String email, @NotEmpty String password) {
    }

    private record LoginResponse(String accessToken, String refreshToken) {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException() {
        return ResponseEntity.notFound().build();
    }
}
