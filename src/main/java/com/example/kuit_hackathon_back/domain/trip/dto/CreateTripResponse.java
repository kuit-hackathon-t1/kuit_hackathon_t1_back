package com.example.kuit_hackathon_back.domain.trip.dto;

import java.time.LocalDateTime;

import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;

public record CreateTripResponse(Long tripId, TripStatus status, LocalDateTime createdAt) {

    public static CreateTripResponse from(Trip trip) {
        return new CreateTripResponse(trip.getTripId(), trip.getStatus(), trip.getCreatedAt());
    }
}
