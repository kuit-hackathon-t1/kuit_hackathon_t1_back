package com.example.kuit_hackathon_back.domain.mission.dto;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionCategory;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionGuide;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import java.time.LocalDateTime;
import java.util.List;

public record RandomMissionResponse(
        Long missionId,
        Long tripId,
        String title,
        String description,
        MissionCategory missionCategory,
        MissionStatus missionStatus,
        boolean isLocal,
        List<String> guides,
        LocalDateTime createdAt
) {
    public static RandomMissionResponse from(Mission mission) {
        return new RandomMissionResponse(
                mission.getMissionId(), mission.getTrip().getTripId(), mission.getTitle(), mission.getDescription(),
                mission.getMissionCategory(), mission.getMissionStatus(), mission.isLocal(),
                mission.getGuides().stream().map(MissionGuide::getComment).toList(),
                mission.getCreatedAt());
    }
}
