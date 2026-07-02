package com.example.kuit_hackathon_back.domain.mission.ai;

import com.example.kuit_hackathon_back.domain.trip.entity.CompanionType;
import com.example.kuit_hackathon_back.domain.trip.entity.TripMood;

/** AI에게 미션 배치 생성을 요청할 때 필요한 여행 컨텍스트. */
public record MissionAiRequest(
        String tripName,
        String region,
        CompanionType companionType,
        TripMood mood,
        int count) {}
