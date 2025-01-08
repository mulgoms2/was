package com.example.was.user.controller;

import com.example.was.user.service.UserService;
import com.example.was.user.domain.UserAccount;
import com.example.was.user.exception.UserDuplicateException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/valid")
    public ResponseEntity<?> checkEmailDuplication(@Valid @NotEmpty @Email String email) {
        if (userService.isUserEmailDuplicated(email)) {
            throw new UserDuplicateException(email);
        }

        return ResponseEntity.ok("사용 가능한 유저 이메일입니다");
    }

    @PostMapping("/users")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        if (userService.isUserEmailDuplicated(request.email())) {
            throw new UserDuplicateException("User email is already exist");
        }

        UserAccount account = userService.join(request.toUserAccount());

        return ResponseEntity.ok()
                             .body(new UserCreateResponse(account.getEmail(), account.getUsername()));
    }

    public record UserCreateRequest(@NotEmpty @Email String email, @NotEmpty String name, @NotEmpty String password) {
        private UserAccount toUserAccount() {

            return UserAccount.builder()
                              .username(name)
                              .email(email)
                              .password(password)
                              .build();
        }
    }

    public record UserCreateResponse(String email, String name) {
    }

    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<?> handleUserDuplicateException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("user email already exists");
    }
}