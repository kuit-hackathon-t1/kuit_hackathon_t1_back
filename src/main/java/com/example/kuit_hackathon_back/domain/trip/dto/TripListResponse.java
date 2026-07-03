package com.example.kuit_hackathon_back.domain.trip.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.kuit_hackathon_back.domain.trip.entity.CompanionType;
import com.example.kuit_hackathon_back.domain.trip.entity.TripMood;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;

public record TripListResponse(List<TripSummary> trips) {

    public static TripListResponse of(List<TripSummary> trips) {
        return new TripListResponse(trips);
    }

    public record TripSummary(
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
            long totalCollectionCount) {}
}
