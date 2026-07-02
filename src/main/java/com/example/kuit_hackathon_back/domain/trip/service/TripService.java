package com.example.kuit_hackathon_back.domain.trip.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.collection.repository.CollectionRepository;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import com.example.kuit_hackathon_back.domain.mission.repository.MissionRepository;
import com.example.kuit_hackathon_back.domain.trip.dto.CreateTripRequest;
import com.example.kuit_hackathon_back.domain.trip.dto.CreateTripResponse;
import com.example.kuit_hackathon_back.domain.trip.dto.CurrentTripResponse;
import com.example.kuit_hackathon_back.domain.trip.dto.CurrentTripResponse.CollectionSummary;
import com.example.kuit_hackathon_back.domain.trip.dto.CurrentTripResponse.MissionSummary;
import com.example.kuit_hackathon_back.domain.trip.dto.EndTripResponse;
import com.example.kuit_hackathon_back.domain.trip.dto.TripReviewResponse;
import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.entity.TripStatus;
import com.example.kuit_hackathon_back.domain.trip.repository.TripRepository;
import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.domain.user.repository.UserRepository;
import com.example.kuit_hackathon_back.global.exception.BusinessException;
import com.example.kuit_hackathon_back.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final CollectionRepository collectionRepository;

    public CurrentTripResponse getCurrentTrip(Long userId) {
        return tripRepository
                .findByUser_UserIdAndStatus(userId, TripStatus.ACTIVE)
                .map(
                        trip ->
                                CurrentTripResponse.of(
                                        trip,
                                        buildMissionSummary(trip.getTripId()),
                                        buildCollectionSummary(trip.getTripId())))
                .orElseGet(CurrentTripResponse::empty);
    }

    @Transactional
    public CreateTripResponse createTrip(Long userId, CreateTripRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
            throw new BusinessException(
                    ErrorCode.VALIDATION_ERROR,
                    "여행 기간이 올바르지 않습니다.",
                    Map.of("startDate", "시작일은 종료일보다 늦을 수 없습니다."));
        }
        if (tripRepository.existsByUser_UserIdAndStatus(userId, TripStatus.ACTIVE)) {
            throw new BusinessException(ErrorCode.ACTIVE_TRIP_ALREADY_EXISTS);
        }
        User user = getUserOrThrow(userId);
        Trip trip =
                Trip.builder()
                        .tripName(request.tripName())
                        .region(request.region())
                        .startDate(request.startDate())
                        .endDate(request.endDate())
                        .companionType(request.companionType())
                        .mood(request.mood())
                        .user(user)
                        .build();
        return CreateTripResponse.from(tripRepository.save(trip));
    }

    @Transactional
    public EndTripResponse endTrip(Long userId, Long tripId) {
        Trip trip = getOwnedTripOrThrow(userId, tripId);
        trip.end();
        return EndTripResponse.from(trip);
    }

    public TripReviewResponse getTripReview(Long userId, Long tripId) {
        Trip trip = getOwnedTripOrThrow(userId, tripId);
        long success =
                missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.SUCCESS);
        long failed =
                missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.FAILURE);
        long total = missionRepository.countByTrip_TripId(tripId);
        long totalCollections = collectionRepository.countByTrip_TripId(tripId);
        return new TripReviewResponse(
                trip.getTripId(),
                trip.getTripName(),
                trip.getRegion(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getCompanionType(),
                trip.getMood(),
                trip.getStatus(),
                success,
                failed,
                total,
                totalCollections);
    }

    private MissionSummary buildMissionSummary(Long tripId) {
        long total = missionRepository.countByTrip_TripId(tripId);
        long active =
                missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.ACTIVE);
        long success =
                missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.SUCCESS);
        long failed =
                missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.FAILURE);
        return new MissionSummary(total, active, success, failed);
    }

    private CollectionSummary buildCollectionSummary(Long tripId) {
        long total = collectionRepository.countByTrip_TripId(tripId);
        long success =
                collectionRepository.countByTrip_TripIdAndStatus(tripId, CollectionStatus.SUCCESS);
        long failed =
                collectionRepository.countByTrip_TripIdAndStatus(tripId, CollectionStatus.FAILURE);
        return new CollectionSummary(total, success, failed);
    }

    private Trip getOwnedTripOrThrow(Long userId, Long tripId) {
        Trip trip =
                tripRepository
                        .findById(tripId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        if (!trip.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.TRIP_NOT_FOUND);
        }
        return trip;
    }

    private User getUserOrThrow(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
