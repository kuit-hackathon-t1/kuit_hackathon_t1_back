package com.example.kuit_hackathon_back.domain.mission.ai;

import java.util.List;

/**
 * 여행 정보를 바탕으로 미션 추천 배치를 생성하는 AI 클라이언트 계약.
 *
 * <p>구현체를 교체(Gemini -> Claude/OpenAI 등)해도 이 인터페이스만 바라보는 상위 로직(MissionService)은 영향을 받지 않는다.
 */
public interface MissionAiClient {

    /**
     * 여행 컨텍스트에 맞는 미션 후보 목록을 생성한다.
     *
     * @throws MissionAiGenerationException AI 호출/파싱에 실패한 경우
     */
    List<GeneratedMission> generateMissions(MissionAiRequest request);
}
