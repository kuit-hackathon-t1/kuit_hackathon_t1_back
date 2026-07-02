package com.example.kuit_hackathon_back.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 1, max = 15, message = "닉네임은 1자 이상 15자 이하입니다.")
        String nickname
) {
}
