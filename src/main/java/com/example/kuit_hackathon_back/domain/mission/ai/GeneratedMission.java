package com.example.kuit_hackathon_back.domain.mission.ai;

import java.util.List;

import com.example.kuit_hackathon_back.domain.mission.entity.MissionCategory;

/** AI가 생성한 미션 후보 하나를 나타내는 값 객체. */
public record GeneratedMission(
        String title,
        String description,
        MissionCategory missionCategory,
        boolean isLocal,
        List<String> guides) {}
