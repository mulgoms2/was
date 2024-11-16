package com.example.was.auth.controller;

import com.example.was.auth.service.AuthService;
import com.example.was.auth.service.JwtTokenProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            UserDetails userDetails = authService.loadUserByUsername(loginRequest.email());

            if (!passwordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
                return ResponseEntity.status(401)
                        .body("Invalid username or password");
            }
            String jwtToken = jwtTokenProvider.createToken(loginRequest.email());

            return ResponseEntity.ok()
                    .body(jwtToken);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401)
                    .body("일치하는 사용자가 없어요");
        }
    }

    private record LoginRequest(@NotEmpty @Email String email, @NotEmpty String password) {
    }
}
