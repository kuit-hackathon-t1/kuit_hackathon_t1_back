-- API 설계서(Enum 정의)에 맞춰 컬럼 코멘트를 최신화한다.
-- 컬럼 타입/제약조건 변경은 없다 (코멘트만 갱신).

ALTER TABLE trip
    MODIFY COLUMN mood VARCHAR(30) NULL
        COMMENT '여행 분위기 - EMOTIONAL/WANDERING/LOCAL/COURAGE';

ALTER TABLE mission
    MODIFY COLUMN mission_category VARCHAR(30) NOT NULL
        COMMENT '미션 종류 - OBSERVATION/ACTION/LOCAL/RANDOM';

ALTER TABLE mission
    MODIFY COLUMN mission_status VARCHAR(30) NOT NULL
        COMMENT '미션 상태 - RECOMMENDED/ACTIVE/SUCCESS/FAILURE';

-- 주의: ERD 피드백 단계에서는 crop_type을 BUTTERFLY/MOTH/SNAIL/BEETLE로 정리했으나,
-- 이후 API 설계서(Enum 정의)에서 BUTTERFLY/BEETLE/DRAGONFLY로 확정되어 이를 최종값으로 반영한다.
ALTER TABLE collection
    MODIFY COLUMN crop_type VARCHAR(50) NOT NULL
        COMMENT '채집틀에 저장되는 프레임 종류 - BUTTERFLY/BEETLE/DRAGONFLY';
