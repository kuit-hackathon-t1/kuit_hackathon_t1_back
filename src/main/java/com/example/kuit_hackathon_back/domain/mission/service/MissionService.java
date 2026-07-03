package com.example.kuit_hackathon_back.domain.mission.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kuit_hackathon_back.domain.mission.ai.GeneratedMission;
import com.example.kuit_hackathon_back.domain.mission.ai.MissionAiClient;
import com.example.kuit_hackathon_back.domain.mission.ai.MissionAiRequest;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionDetailResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionListResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionStartResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionTemplateProvider;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionTemplateProvider.MissionTemplate;
import com.example.kuit_hackathon_back.domain.mission.dto.RandomMissionResponse;
import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionCategory;
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

    private static final Logger log = LoggerFactory.getLogger(MissionService.class);

    /** 여행당 "시작"한(ACTIVE로 전환되었거나 이미 완료된) 미션 수의 상한. 뽑기만 반복하는 것은 이 한도를 소모하지 않는다. */
    private static final int MAX_MISSIONS_PER_TRIP = 12;
    private static final int MAX_ACTIVE_MISSIONS_PER_TRIP = 4;
    private static final int MISSION_BATCH_SIZE = 15;

    private final MissionRepository missionRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final MissionTemplateProvider templateProvider;
    private final MissionAiClient missionAiClient;

    private final SecureRandom random = new SecureRandom();

    @Transactional
    public RandomMissionResponse createRandomMission(Long userId, Long tripId) {
        User user = getUserOrThrow(userId);
        Trip trip = getOwnedTripOrThrow(userId, tripId);
        if (!trip.isActive()) {
            throw new BusinessException(ErrorCode.TRIP_ALREADY_ENDED, "종료된 여행에서는 미션을 생성할 수 없습니다.");
        }
        if (missionRepository.countByTrip_TripIdAndMissionStatusNot(tripId, MissionStatus.RECOMMENDED)
                >= MAX_MISSIONS_PER_TRIP) {
            throw new BusinessException(ErrorCode.MISSION_LIMIT_EXCEEDED);
        }
        if (missionRepository.countByTrip_TripIdAndMissionStatus(tripId, MissionStatus.ACTIVE)
                >= MAX_ACTIVE_MISSIONS_PER_TRIP) {
            throw new BusinessException(ErrorCode.ACTIVE_MISSION_LIMIT_EXCEEDED);
        }

        List<Mission> pool = missionRepository.findByTrip_TripIdAndDrawnAtIsNull(tripId);
        if (pool.isEmpty()) {
            pool = generateMissionPool(user, trip);
        }

        Mission drawn = pool.get(random.nextInt(pool.size()));
        drawn.draw();
        missionRepository.save(drawn);

        return RandomMissionResponse.from(drawn);
    }

    /** AI로 미션 배치를 생성해서 저장한다. AI 호출이 실패하면 하드코딩된 템플릿 풀로 폴백한다. */
    private List<Mission> generateMissionPool(User user, Trip trip) {
        try {
            MissionAiRequest aiRequest =
                    new MissionAiRequest(
                            trip.getTripName(),
                            trip.getRegion(),
                            trip.getCompanionType(),
                            trip.getMood(),
                            MISSION_BATCH_SIZE);
            List<GeneratedMission> generated = missionAiClient.generateMissions(aiRequest);
            List<Mission> missions = new ArrayList<>();
            for (GeneratedMission g : generated) {
                missions.add(buildMissionFrom(user, trip, g));
            }
            return missionRepository.saveAll(missions);
        } catch (RuntimeException e) {
            log.warn(
                    "AI 미션 생성 실패, 하드코딩 템플릿으로 폴백합니다. tripId={}, cause={}",
                    trip.getTripId(),
                    e.toString(),
                    e);
            List<Mission> fallback = new ArrayList<>();
            for (MissionTemplate t : templateProvider.getAllTemplates()) {
                fallback.add(buildMissionFrom(user, trip, t));
            }
            return missionRepository.saveAll(fallback);
        }
    }

    private Mission buildMissionFrom(User user, Trip trip, GeneratedMission generated) {
        return buildMission(
                user,
                trip,
                generated.title(),
                generated.description(),
                generated.missionCategory(),
                generated.isLocal(),
                generated.guides());
    }

    private Mission buildMissionFrom(User user, Trip trip, MissionTemplate template) {
        return buildMission(
                user,
                trip,
                template.title(),
                template.description(),
                template.category(),
                template.isLocal(),
                template.guides());
    }

    private Mission buildMission(
            User user,
            Trip trip,
            String title,
            String description,
            MissionCategory category,
            boolean isLocal,
            List<String> guides) {
        Mission mission =
                Mission.builder()
                        .title(title)
                        .description(description)
                        .missionCategory(category)
                        .local(isLocal)
                        .user(user)
                        .trip(trip)
                        .build();
        guides.forEach(mission::addGuide);
        return mission;
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
        List<Mission> missions;
        if (status == null) {
            missions = missionRepository.findByTrip_TripIdAndDrawnAtIsNotNull(tripId);
        } else {
            missions =
                    missionRepository.findByTrip_TripIdAndMissionStatusAndDrawnAtIsNotNull(
                            tripId, status);
        }
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
                        .filter(Mission::isDrawn)
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
