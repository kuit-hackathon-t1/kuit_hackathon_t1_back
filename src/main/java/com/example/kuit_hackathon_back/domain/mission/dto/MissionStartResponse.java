package com.example.kuit_hackathon_back.domain.mission.dto;

import java.time.LocalDateTime;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;

public record MissionStartResponse(
        Long missionId, MissionStatus missionStatus, LocalDateTime updatedAt) {

    public static MissionStartResponse from(Mission mission) {
        return new MissionStartResponse(
                mission.getMissionId(), mission.getMissionStatus(), mission.getUpdatedAt());
    }
}
