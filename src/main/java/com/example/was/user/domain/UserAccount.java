package com.example.was.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    @ElementCollection
    private List<GrantedAuthority> authorities = new ArrayList<>();
    @ElementCollection
    private List<GrantedAuthority> role = new ArrayList<>();

    @Builder
    public UserAccount(String username, String email, String password, List<GrantedAuthority> authorities, List<GrantedAuthority> role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.role = role;
    }
}
