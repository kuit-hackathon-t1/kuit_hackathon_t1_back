package com.example.kuit_hackathon_back.domain.collection.dto;

import com.example.kuit_hackathon_back.domain.collection.entity.Collection;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import java.time.LocalDateTime;

public record CreateCollectionResponse(Long collectionId, Long missionId, MissionStatus missionStatus,
        LocalDateTime createdAt) {

    public static CreateCollectionResponse from(Collection collection) {
        return new CreateCollectionResponse(collection.getCollectionId(), collection.getMission().getMissionId(),
                collection.getMission().getMissionStatus(), collection.getCreatedAt());
    }
}
