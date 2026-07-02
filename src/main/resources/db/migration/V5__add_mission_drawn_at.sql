-- AI 배치 추천 도입: mission 테이블에 '실제로 뽑혀서 사용자에게 노출된 시각'을 기록하는 컬럼을 추가한다.
-- NULL이면 아직 추천 풀에만 존재하고 사용자에게 보여준 적 없는 미션이다.

ALTER TABLE mission
    ADD COLUMN drawn_at DATETIME NULL COMMENT '사용자에게 실제로 뽑혀서 노출된 시각 (NULL이면 추천 풀에만 존재)'
    AFTER is_local;

CREATE INDEX idx_mission_trip_drawn ON mission (trip_id, drawn_at);
