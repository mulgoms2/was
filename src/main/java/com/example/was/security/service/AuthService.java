package com.example.was.security.service;

import com.example.was.user.domain.UserAccount;
import com.example.was.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount userAccount = getUserAccount(email);

        log.info("{}", userAccount);

        List<GrantedAuthority> authorities = userAccount.getAuthorities();
        List<GrantedAuthority> role = userAccount.getRole();

        List<GrantedAuthority> roleAndAuthorities = new ArrayList<>();
        roleAndAuthorities.addAll(authorities);
        roleAndAuthorities.addAll(role);

        return new User(userAccount.getEmail(), userAccount.getPassword(), roleAndAuthorities);
    }

    private UserAccount getUserAccount(String username) {
        return userService.getUserAccount(username);
    }
}
