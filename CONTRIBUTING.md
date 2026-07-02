# Contributing Guide

이 문서는 [kuit-hackathon-t1-front](https://github.com/kuit-hackathon-t1/kuit-hackathon-t1-front) 레포의 컨벤션(ESLint/Prettier, PR 템플릿, GitHub Actions CI)을 Spring Boot 백엔드 환경에 맞게 통일한 규칙입니다.

## 브랜치 전략

- `main`: 배포 브랜치
- `develop`: 개발 통합 브랜치 (PR은 항상 이 브랜치를 대상으로 생성)
- 작업 브랜치: `{type}/{description}` 예) `feat/mission-api`, `fix/login-token`

## 커밋 컨벤션

프론트와 동일한 타입을 사용합니다 (`.github/pull_request_template.md`의 PR 유형과 일치).

| 타입 | 설명 |
| --- | --- |
| Feat | 기능 추가 |
| Fix | 버그 수정 |
| Refactor | 리팩토링 |
| Docs | 문서 수정 |
| Chore | 설정 변경 / 의존성 추가 |

형식: `{Type}: {변경 내용 요약}` 예) `Feat: 미션 추천 API 추가`

## 코드 스타일

- `.editorconfig`로 기본 들여쓰기·개행·인코딩 규칙을 통일합니다 (Java 4-space, JSON/YAML 2-space).
- Java 포맷팅은 [Spotless](https://github.com/diffplug/spotless)(Google Java Format, AOSP 스타일)로 자동화되어 있으며, 프론트의 ESLint/Prettier에 대응합니다.
  - 스타일 검사: `./gradlew spotlessCheck`
  - 자동 정렬: `./gradlew spotlessApply`
- PR 생성 전 로컬에서 `./gradlew spotlessApply`를 실행해 스타일을 맞춰 주세요.

## PR 규칙

- PR을 생성하면 `.github/pull_request_template.md`가 자동 적용됩니다.
- PR 생성/업데이트 시 `.github/workflows/backend-ci.yml`이 자동으로 아래를 검사합니다.
  1. 코드 스타일 검사 (`spotlessCheck`)
  2. 테스트 실행 (`test`)
  3. 빌드 성공 여부 (`build`)
- 위 검사가 모두 통과해야 머지 가능합니다 (프론트 `frontend-ci.yml`과 동일한 트리거 구조: `develop`, `main` 대상 PR/push).

## 참고

- 기존에 있던 `.editconfig`(오타)는 EditorConfig 도구가 인식하지 못해 동작하지 않으므로, 이번에 추가한 `.editorconfig`가 실제로 적용되는 설정 파일입니다. 확인 후 `.editconfig`는 삭제해 주세요.
