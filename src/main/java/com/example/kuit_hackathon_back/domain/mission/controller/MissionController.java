package com.example.kuit_hackathon_back.domain.mission.controller;

import com.example.kuit_hackathon_back.domain.mission.dto.MissionDetailResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionListResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.MissionStartResponse;
import com.example.kuit_hackathon_back.domain.mission.dto.RandomMissionResponse;
import com.example.kuit_hackathon_back.domain.mission.entity.MissionStatus;
import com.example.kuit_hackathon_back.domain.mission.service.MissionService;
import com.example.kuit_hackathon_back.global.resolver.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/random")
    public ResponseEntity<RandomMissionResponse> createRandomMission(@CurrentUserId Long userId,
            @RequestParam Long tripId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(missionService.createRandomMission(userId, tripId));
    }

    @PatchMapping("/{missionId}/start")
    public ResponseEntity<MissionStartResponse> startMission(@CurrentUserId Long userId,
            @PathVariable Long missionId) {
        return ResponseEntity.ok(missionService.startMission(userId, missionId));
    }

    @GetMapping
    public ResponseEntity<MissionListResponse> getMissions(@CurrentUserId Long userId,
            @RequestParam Long tripId, @RequestParam(required = false) MissionStatus status) {
        return ResponseEntity.ok(missionService.getMissions(userId, tripId, status));
    }

    @GetMapping("/{missionId}")
    public ResponseEntity<MissionDetailResponse> getMissionDetail(@CurrentUserId Long userId,
            @PathVariable Long missionId) {
        return ResponseEntity.ok(missionService.getMissionDetail(userId, missionId));
    }
}
