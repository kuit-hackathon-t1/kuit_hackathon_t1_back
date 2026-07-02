package com.example.kuit_hackathon_back.domain.trip.dto;

import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;
import java.time.LocalDateTime;

public record EndTripResponse(Long tripId, TripStatus status, LocalDateTime updatedAt) {

    public static EndTripResponse from(Trip trip) {
        return new EndTripResponse(trip.getTripId(), trip.getStatus(), trip.getUpdatedAt());
    }
}
