package com.example.kuit_hackathon_back.domain.mission.dto;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionCategory;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import java.time.LocalDateTime;
import java.util.List;

public record MissionListResponse(Long tripId, List<MissionItem> missions) {

    public static MissionListResponse of(Long tripId, List<Mission> missions) {
        return new MissionListResponse(tripId, missions.stream().map(MissionItem::from).toList());
    }

    public record MissionItem(
            Long missionId,
            String title,
            String description,
            MissionCategory missionCategory,
            MissionStatus missionStatus,
            boolean isLocal,
            LocalDateTime createdAt
    ) {
        public static MissionItem from(Mission mission) {
            return new MissionItem(mission.getMissionId(), mission.getTitle(), mission.getDescription(),
                    mission.getMissionCategory(), mission.getMissionStatus(), mission.isLocal(),
                    mission.getCreatedAt());
        }
    }
}
