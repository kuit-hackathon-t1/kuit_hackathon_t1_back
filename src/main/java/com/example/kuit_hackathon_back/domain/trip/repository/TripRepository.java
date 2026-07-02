package com.example.kuit_hackathon_back.domain.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByUser_UserIdAndStatus(Long userId, TripStatus status);

    boolean existsByUser_UserIdAndStatus(Long userId, TripStatus status);
}
