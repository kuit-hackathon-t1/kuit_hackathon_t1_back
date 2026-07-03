-- 컬럼 코멘트를 최신 Enum 정의에 맞춰 일괄 갱신한다.
-- 컬럼 타입/제약조건 변경은 없다 (코멘트만 갱신).

-- CompanionType: ETC -> FAMILY로 변경, LOVER 오타(COUPLE) 수정
ALTER TABLE trip
    MODIFY COLUMN companion_type VARCHAR(20) NOT NULL
        COMMENT '동행자 - ALONE/FRIEND/COUPLE/FAMILY.';

-- CropType: BUTTERFLY/BEETLE/DRAGONFLY로 확정했던 것을 BUTTERFLY/MOTH/SNAIL/BEETLE로 재확정
ALTER TABLE collection
    MODIFY COLUMN crop_type VARCHAR(50) NOT NULL
        COMMENT '채집틀에 저장되는 프레임 종류 - BUTTERFLY/MOTH/SNAIL/BEETLE';
