-- =========================================================
-- KUIT Hackathon T1 Backend - Initial Schema
-- 피드백 반영 버전 (User/Trip/Mission/MissionGuide/Collection/Emotion)
-- 문자셋: utf8mb4 (한글/이모지 포함 대응)
-- =========================================================

-- ---------------------------------------------------------
-- user
--   - nickname UNIQUE 추가
-- ---------------------------------------------------------
CREATE TABLE `user` (
    user_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname   VARCHAR(15) NOT NULL COMMENT '로그인 절차 X, 사용자 구분을 위한 attribute',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입 시점',
    CONSTRAINT uq_user_nickname UNIQUE (nickname)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ---------------------------------------------------------
-- trip
--   - tripname -> trip_name
--   - attraction -> region
--   - companion -> companion_type
--   - mood 컬럼 추가 (값 목록 미정, 우선 nullable VARCHAR(30)으로 생성 - 추후 확정 필요)
--   - status 값 ACTIVE/ENDED (PLANNING 제거)
-- ---------------------------------------------------------
CREATE TABLE trip (
    trip_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_name       VARCHAR(60) NOT NULL COMMENT '여행 이름',
    region          VARCHAR(75) NOT NULL COMMENT '여행지',
    start_date      DATE NOT NULL COMMENT '여행 시작일자',
    end_date        DATE NOT NULL COMMENT '여행 종료일자',
    companion_type  VARCHAR(20) NOT NULL COMMENT '동행자 - ALONE/FRIEND/LOVER etc.',
    mood            VARCHAR(30) NULL COMMENT '여행 분위기/무드 - 값 목록 확정 필요',
    status          VARCHAR(10) NOT NULL COMMENT '여행 상태 - ACTIVE/ENDED',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id         BIGINT NOT NULL COMMENT '사용자 fk',
    CONSTRAINT fk_trip_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_trip_user_id ON trip (user_id);

-- ---------------------------------------------------------
-- mission
--   - mission_status 값 RECOMMENDED/ACTIVE/SUCCESS/FAILURE
--   - mission_category 값 목록 미정 (추후 확정 필요)
-- ---------------------------------------------------------
CREATE TABLE mission (
    mission_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(150) NOT NULL COMMENT '미션 제목',
    description      VARCHAR(600) NOT NULL COMMENT '미션 설명',
    mission_category VARCHAR(30) NOT NULL COMMENT '미션 종류 - 값 목록 확정 필요',
    mission_status   VARCHAR(30) NOT NULL COMMENT '미션 상태 - RECOMMENDED/ACTIVE/SUCCESS/FAILURE',
    is_local         BOOLEAN NOT NULL DEFAULT FALSE COMMENT '지역 미션 여부',
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id          BIGINT NOT NULL,
    trip_id          BIGINT NOT NULL,
    CONSTRAINT fk_mission_user FOREIGN KEY (user_id) REFERENCES `user` (user_id),
    CONSTRAINT fk_mission_trip FOREIGN KEY (trip_id) REFERENCES trip (trip_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_mission_user_id ON mission (user_id);
CREATE INDEX idx_mission_trip_id ON mission (trip_id);

-- ---------------------------------------------------------
-- mission_guide (구 Mission_guide)
--   - 테이블명 mission_guide로 변경
--   - Mission : MissionGuide = 1 : N 관계 (기존 오표기 수정)
-- ---------------------------------------------------------
CREATE TABLE mission_guide (
    guide_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    comment    VARCHAR(150) NOT NULL COMMENT '가이드 문장',
    CONSTRAINT fk_mission_guide_mission FOREIGN KEY (mission_id) REFERENCES mission (mission_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_mission_guide_mission_id ON mission_guide (mission_id);

-- ---------------------------------------------------------
-- collection
--   - memo 150 -> 500
--   - image_id -> local_image_id (프론트 IndexedDB에 저장되는 로컬 이미지 참조 id)
--   - status 값 SUCCESS/FAILURE (UNPROCESSED 제거)
--   - mission_id UNIQUE 추가 (Mission : Collection = 1 : 1)
--   - updated_at 추가
--   - crop_type 유지
-- ---------------------------------------------------------
CREATE TABLE collection (
    collection_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    memo            VARCHAR(500) NOT NULL COMMENT '한줄평',
    local_image_id  VARCHAR(255) NOT NULL COMMENT '프론트(IndexedDB)에 저장된 이미지의 local id 참조값',
    status          VARCHAR(20) NOT NULL COMMENT '미션 성공/실패 여부 - SUCCESS/FAILURE',
    mission_id      BIGINT NOT NULL,
    trip_id         BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    crop_type       VARCHAR(50) NOT NULL COMMENT '채집틀에 저장되는 프레임 종류 - BUTTERFLY/MOTH/SNAIL/BEETLE',
    CONSTRAINT uq_collection_mission_id UNIQUE (mission_id),
    CONSTRAINT fk_collection_mission FOREIGN KEY (mission_id) REFERENCES mission (mission_id),
    CONSTRAINT fk_collection_trip FOREIGN KEY (trip_id) REFERENCES trip (trip_id),
    CONSTRAINT fk_collection_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_collection_trip_id ON collection (trip_id);
CREATE INDEX idx_collection_user_id ON collection (user_id);

-- ---------------------------------------------------------
-- emotion
--   - 유지 (emotionTags 빈 배열이면 row 0개 -> 스키마 변경 없음)
-- ---------------------------------------------------------
CREATE TABLE emotion (
    emotion_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    collection_id BIGINT NOT NULL,
    tag           VARCHAR(30) NOT NULL COMMENT '태그된 감정',
    CONSTRAINT fk_emotion_collection FOREIGN KEY (collection_id) REFERENCES collection (collection_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_emotion_collection_id ON emotion (collection_id);
