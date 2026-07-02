package com.example.kuit_hackathon_back.domain.collection.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kuit_hackathon_back.domain.collection.entity.Collection;
import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByTrip_TripId(Long tripId);

    List<Collection> findByTrip_TripIdAndStatus(Long tripId, CollectionStatus status);

    long countByTrip_TripId(Long tripId);

    long countByTrip_TripIdAndStatus(Long tripId, CollectionStatus status);
}
