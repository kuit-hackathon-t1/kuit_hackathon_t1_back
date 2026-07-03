package com.example.kuit_hackathon_back.domain.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByTrip_TripId(Long tripId);

    List<Mission> findByTrip_TripIdAndMissionStatus(Long tripId, MissionStatus missionStatus);

    long countByTrip_TripId(Long tripId);

    long countByTrip_TripIdAndMissionStatus(Long tripId, MissionStatus missionStatus);

    /** 사용자에게 아직 노출되지 않은 AI 추천 풀. */
    List<Mission> findByTrip_TripIdAndDrawnAtIsNull(Long tripId);

    /** 실제로 뽑혀서 사용자에게 노출된 미션만 조회할 때 사용한다. */
    List<Mission> findByTrip_TripIdAndDrawnAtIsNotNull(Long tripId);

    List<Mission> findByTrip_TripIdAndMissionStatusAndDrawnAtIsNotNull(
            Long tripId, MissionStatus missionStatus);

    long countByTrip_TripIdAndDrawnAtIsNotNull(Long tripId);

    /** RECOMMENDED(아직 시작 안 함)를 제외한, 실제로 시작(ACTIVE)되었거나 완료(SUCCESS/FAILURE)된 미션 수. */
    long countByTrip_TripIdAndMissionStatusNot(Long tripId, MissionStatus missionStatus);
}
