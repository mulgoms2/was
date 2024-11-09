package com.example.was.login.controller;

import com.example.was.login.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok().body("jws");
    }

    @PostMapping("/create/member")
    public ResponseEntity<String> createMember(MemberDto member) {
        UserDetails user;
        return ResponseEntity.ok().body("member");
    }
}
