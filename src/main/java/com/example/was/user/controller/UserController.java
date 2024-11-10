package com.example.was.user.controller;

import com.example.was.user.domain.UserAccount;
import com.example.was.user.dto.UserDto;
import com.example.was.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserAccount account = userService.join(request.toUserAccount());

        return ResponseEntity.ok()
                .body(new UserCreateResponse(account.getEmail(), account.getUsername()));
    }

    private record UserCreateRequest(@NotEmpty String email, @NotEmpty String name, @NotEmpty String password) {
        private UserAccount toUserAccount() {
            return UserAccount.builder()
                    .username(name)
                    .email(email)
                    .password(password)
                    .build();
        }
    }

    private record UserCreateResponse(String email, String name) {
    }
}