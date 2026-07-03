# KUIT Hackathon T1 Backend

청춘의 시간을 작은 여행 미션으로 남기는 해커톤 백엔드 프로젝트입니다.

사용자는 여행 스타일을 선택하고, 추천받은 미션을 채집해 보관할 수 있습니다.  
여행을 진행하며 쌓은 채집 표본과 그에 담긴 추억들이 채집함에 쌓입니다.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Gradle
- Lombok
- H2 / MySQL
- Spotless (Google Java Format)
- EditorConfig

## Getting Started

```
./gradlew bootRun
```

## AI 미션 추천 (Gemini)

여행 생성 후 첫 미션을 뽑을 때(`POST /api/v1/missions/random`), 그 여행의 첫 뽑기 요청 시점에 Gemini API를 1회 호출해
여행 정보(지역/분위기/동행 유형)에 맞는 미션 15개를 배치로 생성해 저장해두고, 이후 뽑기는 이 풀에서 꺼내 씁니다.

로컬에서 실제 AI 추천을 테스트하려면 환경변수로 API 키를 주입하세요 (키를 코드/설정 파일에 직접 커밋하지 마세요).

```
GEMINI_API_KEY=발급받은_키 ./gradlew bootRun --args='--spring.profiles.active=local'
```

키가 없거나 호출이 실패하면 하드코딩된 미션 템플릿으로 자동 폴백되어 서버가 죽지 않습니다.

## Convention

코드 스타일, 커밋/PR 컨벤션, CI 자동화는 [CONTRIBUTING.md](./CONTRIBUTING.md)를 참고해 주세요.

## About

쿠잇 해커톤1팀 백엔드 레포지토리입니다.

## 고민했던 부분

1. 여행 완료 시 기록을 남기지 않았던 미션들의 정보가 누락되는 현상 발생

→ 여행 완료시까지 기록을 남기지 않은 미션들은 채집함에서 볼 수 없도록 하고, 통계에서도 표시되지 않습니다.

2. 다시 뽑기 기능을 수행할 때 다시 뽑기를 희망한 미션과 다시 뽑기를 진행하지 않는 미션을 구분하지 않아 중복으로 미션이 출력되는 현상 발생

→ 추천 미션 목록에 존재하나 사용자의 확인 여부를 나타내기 위한 drawn_at이라는 attribute를 추가

3. 동시에 진행 중인(ACTIVE) 미션이 계속 쌓여 관리가 안 되는 현상 발생

→ 새 미션을 뽑을 때(POST /missions/random) 해당 여행의 ACTIVE 상태 미션 수를 먼저 세어, 4개 이상이면 뽑기 자체를 막도록 처리(ACTIVE_MISSION_LIMIT_EXCEEDED). 시작 단계가 아니라 뽑기 단계에서 선제 차단하여 사용자가 벌여놓기만 하고 완료하지 않는 미션이 무한정 늘어나는 것을 방지.

4. 2번의 drawn_at 기준 12개 제한이 "다시뽑기"만 반복해도 소모되어, 미션을 하나도 시작하지 않았는데 여행이 조기에 뽑기 불가 상태가 되는 현상 발생

→ 12개 제한의 기준을 "뽑힌 미션 수(drawn_at)"에서 "실제로 시작했거나 완료된 미션 수(mission_status != RECOMMENDED)"로 변경. 단순히 다시뽑기만 누르는 행위는 더 이상 한도를 소모하지 않고, 사용자가 실제로 커밋(시작)한 미션 수만 카운트되도록 수정