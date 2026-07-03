package com.example.kuit_hackathon_back.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public LoginResult login(String nickname) {
        return userRepository
                .findByNickname(nickname)
                .map(user -> new LoginResult(user, false))
                .orElseGet(() -> new LoginResult(userRepository.save(User.create(nickname)), true));
    }

    public record LoginResult(User user, boolean created) {}
}
