package com.example.kuit_hackathon_back.domain.collection.dto;

import com.example.kuit_hackathon_back.domain.collection.entity.Collection;
import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.collection.entity.CropType;
import com.example.kuit_hackathon_back.domain.collection.entity.Emotion;
import java.time.LocalDateTime;
import java.util.List;

public record CollectionDetailResponse(
        Long collectionId,
        Long tripId,
        Long missionId,
        String missionTitle,
        String missionDescription,
        String memo,
        String imageId,
        CropType cropType,
        CollectionStatus status,
        LocalDateTime createdAt,
        List<String> emotionTags
) {
    public static CollectionDetailResponse from(Collection collection) {
        return new CollectionDetailResponse(
                collection.getCollectionId(), collection.getTrip().getTripId(),
                collection.getMission().getMissionId(), collection.getMission().getTitle(),
                collection.getMission().getDescription(), collection.getMemo(), collection.getLocalImageId(),
                collection.getCropType(), collection.getStatus(), collection.getCreatedAt(),
                collection.getEmotions().stream().map(Emotion::getTag).toList());
    }
}
