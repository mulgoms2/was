package com.example.was.user.service;

import com.example.was.user.domain.UserAccount;
import com.example.was.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccount join(UserAccount userAccount) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<GrantedAuthority> role = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("READ_PRIVILEGE"));
        role.add(new SimpleGrantedAuthority("ROLE_USER"));

        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccount.setAuthorities(authorities);
        userAccount.setRole(role);

        return userRepository.save(userAccount);
    }

    public boolean isUserEmailDuplicated(String email) {
        return userRepository.findByEmail(email)
                             .isPresent();
    }

    public UserAccount getUserAccount(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
