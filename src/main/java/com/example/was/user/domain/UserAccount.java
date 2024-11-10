package com.example.was.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_ACCOUNT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Builder
    public UserAccount(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
