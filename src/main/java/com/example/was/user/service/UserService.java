package com.example.was.user.service;

import com.example.was.user.domain.UserAccount;
import com.example.was.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserAccount join(UserAccount userAccount) {
        return userRepository.save(userAccount);
    }
}
