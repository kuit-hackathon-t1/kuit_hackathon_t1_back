-- 다시 뽑기(reroll) 기능 추가로 MissionStatus에 CANCELLED 값이 늘어난 것을 반영한다.
-- 컬럼 타입/제약조건 변경은 없다 (코멘트만 갱신).

ALTER TABLE mission
    MODIFY COLUMN mission_status VARCHAR(30) NOT NULL
        COMMENT '미션 상태 - RECOMMENDED/ACTIVE/SUCCESS/FAILURE/CANCELLED';
