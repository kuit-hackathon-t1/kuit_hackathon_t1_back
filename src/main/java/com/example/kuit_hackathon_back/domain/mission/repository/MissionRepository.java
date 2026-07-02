package com.example.kuit_hackathon_back.domain.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByTrip_TripId(Long tripId);

    List<Mission> findByTrip_TripIdAndMissionStatus(Long tripId, MissionStatus missionStatus);

    long countByTrip_TripId(Long tripId);

    long countByTrip_TripIdAndMissionStatus(Long tripId, MissionStatus missionStatus);
}
