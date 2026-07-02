package com.example.kuit_hackathon_back.global.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code, String message, int status, Map<String, String> fieldErrors) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.name(), errorCode.getMessage(), errorCode.getStatus().value(), null);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.name(), message, errorCode.getStatus().value(), null);
    }

    public static ErrorResponse of(
            ErrorCode errorCode, String message, Map<String, String> fieldErrors) {
        return new ErrorResponse(
                errorCode.name(), message, errorCode.getStatus().value(), fieldErrors);
    }

    public static ErrorResponse ofValidation(Map<String, String> fieldErrors) {
        return new ErrorResponse(
                ErrorCode.VALIDATION_ERROR.name(),
                ErrorCode.VALIDATION_ERROR.getMessage(),
                ErrorCode.VALIDATION_ERROR.getStatus().value(),
                fieldErrors);
    }
}
