package com.example.kuit_hackathon_back.domain.trip.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.kuit_hackathon_back.domain.trip.entity.CompanionType;
import com.example.kuit_hackathon_back.domain.trip.entity.TripMood;

public record CreateTripRequest(
        @NotBlank(message = "여행 이름을 입력해주세요.") @Size(max = 60, message = "여행 이름은 60자 이하여야 합니다.")
                String tripName,
        @NotBlank(message = "지역을 입력해주세요.") @Size(max = 75, message = "지역은 75자 이하여야 합니다.")
                String region,
        @NotNull(message = "시작일을 입력해주세요.") LocalDate startDate,
        @NotNull(message = "종료일을 입력해주세요.") LocalDate endDate,
        @NotNull(message = "동행 유형을 선택해주세요.") CompanionType companionType,
        @NotNull(message = "여행 분위기를 선택해주세요.") TripMood mood) {}
