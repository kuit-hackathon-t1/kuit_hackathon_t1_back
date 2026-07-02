package com.example.kuit_hackathon_back.domain.collection.service;

import com.example.kuit_hackathon_back.domain.collection.dto.CollectionDetailResponse;
import com.example.kuit_hackathon_back.domain.collection.dto.CollectionListResponse;
import com.example.kuit_hackathon_back.domain.collection.dto.CreateCollectionRequest;
import com.example.kuit_hackathon_back.domain.collection.dto.CreateCollectionResponse;
import com.example.kuit_hackathon_back.domain.collection.entity.Collection;
import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.collection.repository.CollectionRepository;
import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.repository.MissionRepository;
import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.repository.TripRepository;
import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.domain.user.repository.UserRepository;
import com.example.kuit_hackathon_back.global.exception.BusinessException;
import com.example.kuit_hackathon_back.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final MissionRepository missionRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateCollectionResponse createCollection(Long userId, CreateCollectionRequest request) {
        Mission mission = missionRepository.findById(request.missionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));
        if (!mission.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.MISSION_NOT_FOUND);
        }
        Trip trip = tripRepository.findById(request.tripId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        if (!trip.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.TRIP_NOT_FOUND);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        mission.complete(request.status());

        Collection collection = Collection.builder()
                .memo(request.memo())
                .localImageId(request.imageId())
                .status(request.status())
                .cropType(request.cropType())
                .mission(mission)
                .trip(trip)
                .user(user)
                .build();

        List<String> tags = request.emotionTags() == null ? List.of() : request.emotionTags();
        tags.forEach(collection::addEmotionTag);

        return CreateCollectionResponse.from(collectionRepository.save(collection));
    }

    public CollectionListResponse getCollections(Long userId, Long tripId, CollectionStatus status) {
        getOwnedTripOrThrow(userId, tripId);
        List<Collection> collections = (status == null)
                ? collectionRepository.findByTrip_TripId(tripId)
                : collectionRepository.findByTrip_TripIdAndStatus(tripId, status);
        return CollectionListResponse.of(tripId, collections);
    }

    public CollectionDetailResponse getCollectionDetail(Long userId, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COLLECTION_NOT_FOUND));
        if (!collection.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        return CollectionDetailResponse.from(collection);
    }

    private Trip getOwnedTripOrThrow(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        if (!trip.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.TRIP_NOT_FOUND);
        }
        return trip;
    }
}
