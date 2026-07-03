-- V1에서는 TripMood 값 목록이 확정되지 않아 mood를 nullable로 생성했었다 (추후 확정 필요라고 명시).
-- 이후 TripMood enum(EMOTIONAL/WANDERING/LOCAL/COURAGE)이 확정되었고,
-- CreateTripRequest.mood에도 @NotNull 검증이 걸려 있어 API로 생성되는 trip에는 null이 없다.
-- ERD 설계 의도(NOT NULL)에 맞춰 컬럼 제약도 실제로 반영한다.

ALTER TABLE trip
    MODIFY COLUMN mood VARCHAR(30) NOT NULL COMMENT '여행 분위기 - EMOTIONAL/WANDERING/LOCAL/COURAGE';
