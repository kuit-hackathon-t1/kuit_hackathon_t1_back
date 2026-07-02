package com.example.kuit_hackathon_back.domain.trip.dto;

import com.example.kuit_hackathon_back.domain.trip.entity.CompanionType;
import com.example.kuit_hackathon_back.domain.trip.entity.TripMood;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;
import java.time.LocalDate;

public record TripReviewResponse(
        Long tripId,
        String tripName,
        String region,
        LocalDate startDate,
        LocalDate endDate,
        CompanionType companionType,
        TripMood mood,
        TripStatus status,
        long successMissionCount,
        long failedMissionCount,
        long totalMissionCount,
        long totalCollectionCount
) {
}
