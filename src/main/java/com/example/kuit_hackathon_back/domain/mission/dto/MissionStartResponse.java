package com.example.kuit_hackathon_back.domain.mission.dto;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import java.time.LocalDateTime;

public record MissionStartResponse(Long missionId, MissionStatus missionStatus, LocalDateTime updatedAt) {

    public static MissionStartResponse from(Mission mission) {
        return new MissionStartResponse(mission.getMissionId(), mission.getMissionStatus(), mission.getUpdatedAt());
    }
}
