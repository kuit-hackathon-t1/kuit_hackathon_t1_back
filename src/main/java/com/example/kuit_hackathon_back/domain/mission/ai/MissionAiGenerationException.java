package com.example.kuit_hackathon_back.domain.mission.ai;

/** AI 호출 실패, 응답 파싱 실패 등 미션 배치 생성 중 발생하는 예외. 상위(MissionService)에서 폴백 처리에 사용한다. */
public class MissionAiGenerationException extends RuntimeException {

    public MissionAiGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissionAiGenerationException(String message) {
        super(message);
    }
}
