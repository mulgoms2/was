package com.example.was.user.controller;

import com.example.was.user.domain.UserAccount;
import com.example.was.user.dto.UserDto;
import com.example.was.user.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid UserDto userDto) {
        UserAccount account = userService.join(userDto.toUserAccount());

        // fixme 엔티티를 클라이언트에 노출하면 안된다
        return ResponseEntity.ok()
                .body(new UserCreateResponse(account.getEmail(), account.getUsername()));
    }

    private record UserCreateResponse(String email, String name) {
    }
}