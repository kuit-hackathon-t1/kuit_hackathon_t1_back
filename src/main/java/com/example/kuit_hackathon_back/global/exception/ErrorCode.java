package com.example.kuit_hackathon_back.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 필요합니다."),

    ACTIVE_TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "진행 중인 여행을 찾을 수 없습니다."),
    ACTIVE_TRIP_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 진행 중인 여행이 있습니다."),
    TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "여행을 찾을 수 없습니다."),
    TRIP_ALREADY_ENDED(HttpStatus.CONFLICT, "이미 종료된 여행입니다."),
    TRIP_NOT_ENDED(HttpStatus.CONFLICT, "아직 종료되지 않은 여행입니다."),

    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "미션을 찾을 수 없습니다."),
    MISSION_ALREADY_STARTED(HttpStatus.CONFLICT, "이미 진행 중인 미션입니다."),
    MISSION_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 완료된 미션입니다."),
    MISSION_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "여행 당 미션은 최대 12개까지 생성할 수 있습니다."),
    ACTIVE_MISSION_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "활성화된 미션이 이미 4개입니다. 진행 중인 미션을 먼저 완료해주세요."),

    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "채집 기록을 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
