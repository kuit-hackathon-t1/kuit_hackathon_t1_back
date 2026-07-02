package com.example.kuit_hackathon_back.domain.user.dto;

import java.time.LocalDateTime;

import com.example.kuit_hackathon_back.domain.user.entity.User;

public record LoginResponse(Long userId, String nickname, LocalDateTime createdAt) {

    public static LoginResponse from(User user) {
        return new LoginResponse(user.getUserId(), user.getNickname(), user.getCreatedAt());
    }
}
