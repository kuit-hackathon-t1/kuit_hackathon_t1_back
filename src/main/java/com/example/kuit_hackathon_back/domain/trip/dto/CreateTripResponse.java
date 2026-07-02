package com.example.kuit_hackathon_back.domain.trip.dto;

import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;
import java.time.LocalDateTime;

public record CreateTripResponse(Long tripId, TripStatus status, LocalDateTime createdAt) {

    public static CreateTripResponse from(Trip trip) {
        return new CreateTripResponse(trip.getTripId(), trip.getStatus(), trip.getCreatedAt());
    }
}
