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
