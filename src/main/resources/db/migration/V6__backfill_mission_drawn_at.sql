-- V5에서 drawn_at 컬럼을 NULL 허용으로 추가하면서, AI 배치 추천 기능이 붙기 전에 이미 생성돼 있던
-- 기존 미션들도 전부 drawn_at = NULL이 되어버렸다.
-- 이 미션들은 실제로는 (구버전 로직으로) 이미 사용자에게 노출됐던 미션이므로,
-- '아직 안 뽑힌 AI 추천 풀'과 구분되도록 drawn_at을 created_at으로 백필한다.

UPDATE mission
SET drawn_at = created_at
WHERE drawn_at IS NULL;
