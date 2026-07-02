package com.example.kuit_hackathon_back.domain.collection.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.collection.entity.CropType;

public record CreateCollectionRequest(
        @NotNull(message = "미션 ID를 입력해주세요.") Long missionId,
        @NotNull(message = "여행 ID를 입력해주세요.") Long tripId,
        @NotBlank(message = "한줄평을 입력해주세요.") @Size(max = 500, message = "한줄평은 500자 이하여야 합니다.")
                String memo,
        @NotBlank(message = "이미지 ID를 입력해주세요.") String imageId,
        @NotNull(message = "성공/실패 여부를 선택해주세요.") CollectionStatus status,
        @NotNull(message = "곤충 타입을 선택해주세요.") CropType cropType,
        List<String> emotionTags) {}
