package com.example.kuit_hackathon_back.domain.trip.dto;

import java.time.LocalDateTime;

import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;

public record EndTripResponse(Long tripId, TripStatus status, LocalDateTime updatedAt) {

    public static EndTripResponse from(Trip trip) {
        return new EndTripResponse(trip.getTripId(), trip.getStatus(), trip.getUpdatedAt());
    }
}
