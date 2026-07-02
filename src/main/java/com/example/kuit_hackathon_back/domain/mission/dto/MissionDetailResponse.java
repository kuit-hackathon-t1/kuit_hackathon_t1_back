package com.example.kuit_hackathon_back.domain.mission.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionCategory;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionGuide;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;

public record MissionDetailResponse(
        Long missionId,
        Long tripId,
        String title,
        String description,
        MissionCategory missionCategory,
        MissionStatus missionStatus,
        boolean isLocal,
        List<String> guides,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static MissionDetailResponse from(Mission mission) {
        return new MissionDetailResponse(
                mission.getMissionId(),
                mission.getTrip().getTripId(),
                mission.getTitle(),
                mission.getDescription(),
                mission.getMissionCategory(),
                mission.getMissionStatus(),
                mission.isLocal(),
                mission.getGuides().stream().map(MissionGuide::getComment).toList(),
                mission.getCreatedAt(),
                mission.getUpdatedAt());
    }
}
