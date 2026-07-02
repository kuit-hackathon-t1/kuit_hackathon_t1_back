package com.example.kuit_hackathon_back.domain.collection.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.kuit_hackathon_back.domain.collection.entity.Collection;
import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.collection.entity.CropType;

public record CollectionListResponse(Long tripId, List<CollectionItem> collections) {

    public static CollectionListResponse of(Long tripId, List<Collection> collections) {
        return new CollectionListResponse(
                tripId, collections.stream().map(CollectionItem::from).toList());
    }

    public record CollectionItem(
            Long collectionId,
            String imageId,
            String missionTitle,
            CropType cropType,
            CollectionStatus status,
            LocalDateTime createdAt) {
        public static CollectionItem from(Collection collection) {
            return new CollectionItem(
                    collection.getCollectionId(),
                    collection.getLocalImageId(),
                    collection.getMission().getTitle(),
                    collection.getCropType(),
                    collection.getStatus(),
                    collection.getCreatedAt());
        }
    }
}
