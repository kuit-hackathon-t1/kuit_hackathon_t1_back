package com.example.kuit_hackathon_back.domain.trip.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kuit_hackathon_back.domain.trip.dto.CreateTripRequest;
import com.example.kuit_hackathon_back.domain.trip.dto.CreateTripResponse;
import com.example.kuit_hackathon_back.domain.trip.dto.CurrentTripResponse;
import com.example.kuit_hackathon_back.domain.trip.dto.EndTripResponse;
import com.example.kuit_hackathon_back.domain.trip.dto.TripReviewResponse;
import com.example.kuit_hackathon_back.domain.trip.service.TripService;
import com.example.kuit_hackathon_back.global.resolver.CurrentUserId;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping("/current")
    public ResponseEntity<CurrentTripResponse> getCurrentTrip(@CurrentUserId Long userId) {
        return ResponseEntity.ok(tripService.getCurrentTrip(userId));
    }

    @PostMapping
    public ResponseEntity<CreateTripResponse> createTrip(
            @CurrentUserId Long userId, @Valid @RequestBody CreateTripRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripService.createTrip(userId, request));
    }

    @PostMapping("/{tripId}/end")
    public ResponseEntity<EndTripResponse> endTrip(
            @CurrentUserId Long userId, @PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.endTrip(userId, tripId));
    }

    @GetMapping("/{tripId}/review")
    public ResponseEntity<TripReviewResponse> getTripReview(
            @CurrentUserId Long userId, @PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.getTripReview(userId, tripId));
    }
}
