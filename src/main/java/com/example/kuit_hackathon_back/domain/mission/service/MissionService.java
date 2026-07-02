package com.example.kuit_hackathon_back.domain.mission.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kuit_hackathon_back.domain.mission.dto.MissionDetailResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionListResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionStartResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionTemplateProvider;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionTemplateProvider.MissionTemplate;
import com.example.kuit_hackathon_back.domain.mission.dto.RandomMissionResponse;
import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import com.example.kuit_hackathon_back.domain.mission.repository.MissionRepository;
import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.trip.repository.TripRepository;
import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.domain.user.repository.UserRepository;
import com.example.kuit_hackathon_back.global.exception.BusinessException;
import com.example.kuit_hackathon_back.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private static final int MAX_MISSIONS_PER_TRIP = 12;
    private static final int MAX_ACTIVE_MISSIONS_PER_TRIP = 4;

    private final MissionRepository missionRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final MissionTemplateProvider templateProvider;

    @Transactional
    public RandomMissionResponse createRandomMission(Long userId, Long tripId) {
        User user = getUserOrThrow(userId);
        Trip trip = getOwnedTripOrThrow(userId, tripId);
        if (!trip.isActive()) {
            throw new BusinessException(ErrorCode.TRIP_ALREADY_ENDED, "종료된 여행에서는 미션을 생성할 수 없습니다.");
        }
        if (missionRepository.countByTrip_TripId(tripId) >= MAX_MISSIONS_PER_TRIP) {
            throw new BusinessException(ErrorCode.MISSION_LIMIT_EXCEEDED);
        }
        if (missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.ACTIVE)
                >= MAX_ACTIVE_MISSIONS_PER_TRIP) {
            throw new BusinessException(ErrorCode.ACTIVE_MISSION_LIMIT_EXCEEDED);
        }
        MissionTemplate template = templateProvider.getRandomTemplate();

        Mission mission =
                Mission.builder()
                        .title(template.title())
                        .description(template.description())
                        .missionCategory(template.category())
                        .local(template.isLocal())
                        .user(user)
                        .trip(trip)
                        .build();
        template.guides().forEach(mission::addGuide);

        return RandomMissionResponse.from(missionRepository.save(mission));
    }

    @Transactional
    public MissionStartResponse startMission(Long userId, Long missionId) {
        getUserOrThrow(userId);
        Mission mission = getOwnedMissionOrThrow(userId, missionId);
        mission.start();
        return MissionStartResponse.from(mission);
    }

    public MissionListResponse getMissions(Long userId, Long tripId, MissionStatus status) {
        getUserOrThrow(userId);
        getOwnedTripOrThrow(userId, tripId);
        List<Mission> missions =
                (status == null)
                        ? missionRepository.findByTrip_TripId(tripId)
                        : missionRepository.findByTrip_TripIdAndMissionStatus(tripId, status);
        return MissionListResponse.of(tripId, missions);
    }

    public MissionDetailResponse getMissionDetail(Long userId, Long missionId) {
        getUserOrThrow(userId);
        return MissionDetailResponse.from(getOwnedMissionOrThrow(userId, missionId));
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

    private Mission getOwnedMissionOrThrow(Long userId, Long missionId) {
        Mission mission =
                missionRepository
                        .findById(missionId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));
        if (mission.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.MISSION_NOT_FOUND);
        }
        return mission;
    }

    private User getUserOrThrow(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
