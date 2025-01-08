package com.example.was.user.service;

import com.example.was.user.domain.UserAccount;
import com.example.was.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userAccount = getUserAccount(email);

        log.info("{}", userAccount);

        return userAccount;
    }

    public UserDetails getUserAccount(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public boolean isUserEmailDuplicated(String email) {
        return userRepository.findByEmail(email)
                             .isPresent();
    }

    public UserAccount join(UserAccount userAccount) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("READ_PRIVILEGE"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccount.setAuthorities(authorities);

        return userRepository.save(userAccount);
    }
}
