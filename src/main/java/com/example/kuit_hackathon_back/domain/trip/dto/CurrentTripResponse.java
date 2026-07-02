package com.example.kuit_hackathon_back.domain.trip.dto;

import com.example.kuit_hackathon_back.domain.trip.entity.CompanionType;
import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripMood;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;
import java.time.LocalDate;

public record CurrentTripResponse(boolean hasActiveTrip, TripInfo trip) {

    public static CurrentTripResponse empty() {
        return new CurrentTripResponse(false, null);
    }

    public static CurrentTripResponse of(Trip trip, MissionSummary missionSummary, CollectionSummary collectionSummary) {
        return new CurrentTripResponse(true, TripInfo.of(trip, missionSummary, collectionSummary));
    }

    public record TripInfo(
            Long tripId,
            String tripName,
            String region,
            LocalDate startDate,
            LocalDate endDate,
            CompanionType companionType,
            TripMood mood,
            TripStatus status,
            MissionSummary missionSummary,
            CollectionSummary collectionSummary
    ) {
        public static TripInfo of(Trip trip, MissionSummary missionSummary, CollectionSummary collectionSummary) {
            return new TripInfo(trip.getTripId(), trip.getTripName(), trip.getRegion(), trip.getStartDate(),
                    trip.getEndDate(), trip.getCompanionType(), trip.getMood(), trip.getStatus(),
                    missionSummary, collectionSummary);
        }
    }

    public record MissionSummary(long totalCount, long activeCount, long successCount, long failedCount) {
    }

    public record CollectionSummary(long totalCount, long successCount, long failedCount) {
    }
}
