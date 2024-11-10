package com.example.was.user.dto;

import com.example.was.user.domain.UserAccount;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty
    String name;
    @NotEmpty
    String email;
    @NotEmpty
    String password;

    public UserAccount toUserAccount() {
        return UserAccount.builder()
                .username(name)
                .email(email)
                .password(password)
                .build();
    }
}
