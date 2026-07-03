# DB 생성 및 MySQL 연결 절차

ERD 피드백을 반영한 스키마는 Flyway 마이그레이션(`src/main/resources/db/migration/V1__init_schema.sql`)으로 관리됩니다.
로컬에서 MySQL을 띄우고 서버를 실행하면 테이블이 자동으로 생성됩니다 (수동으로 CREATE TABLE 실행할 필요 없음).

## 1. MySQL 실행 (Docker, 권장)

```
docker compose up -d
```

`docker-compose.yml`이 `kuit_hackathon` 데이터베이스를 가진 MySQL 8.0 컨테이너를 띄웁니다 (root / root1234, 호스트 3307 포트 → 컨테이너 3306 포트).
로컬에 이미 MySQL이 설치되어 3306을 쓰고 있는 경우가 많아 충돌을 피하려고 호스트 포트를 3307로 매핑했습니다.
Docker를 안 쓴다면 로컬에 MySQL을 직접 설치하고 아래 DB만 미리 만들어두면 됩니다.

```sql
CREATE DATABASE kuit_hackathon CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 2. 서버를 `local` 프로필로 실행

```
./gradlew bootRun --args='--spring.profiles.active=local'
```

또는 환경변수로:

```
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

기본값(`application-local.yml`)은 `docker-compose.yml`과 맞춰져 있어 별도 설정 없이 바로 연결됩니다.
접속 정보를 바꾸고 싶다면 아래 환경변수를 사용하세요.

| 환경변수 | 기본값 |
| --- | --- |
| `DB_HOST` | localhost |
| `DB_PORT` | 3307 |
| `DB_NAME` | kuit_hackathon |
| `DB_USERNAME` | root |
| `DB_PASSWORD` | root1234 |

## 3. 정상 연결 확인

서버 실행 로그에 아래와 같은 Flyway 로그가 보이면 스키마 생성 성공입니다.

```
Flyway ... Successfully applied 1 migration to schema `kuit_hackathon`
```

MySQL에 직접 접속해서 테이블 목록을 확인할 수도 있습니다.

```
docker exec -it kuit-hackathon-mysql mysql -uroot -proot1234 kuit_hackathon -e "SHOW TABLES;"
```

`user, trip, mission, mission_guide, collection, emotion` 6개 테이블이 보이면 정상입니다.

## 4. 기본(local 프로필 아닐 때) 동작 - CI/테스트

`spring.profiles.active=local`을 지정하지 않으면(기본 실행, 테스트, CI) Flyway와 MySQL 연결 없이 내장 H2로 동작합니다.
`./gradlew test`나 GitHub Actions CI는 이 기본 설정 그대로 실행되므로 MySQL 없이도 항상 통과합니다.

## 5. 참고 - 이번에 반영된 ERD 피드백

- User: `nickname` UNIQUE 추가
- Trip: `tripname→trip_name`, `attraction→region`, `companion→companion_type` 컬럼명 변경, `mood` 컬럼 추가, `status` 값 `ACTIVE/ENDED`로 정리(`PLANNING` 제거)
- Mission: `mission_status` 값 `RECOMMENDED/ACTIVE/SUCCESS/FAILURE`로 변경, `MissionGuide`와의 관계 1:N으로 수정
- MissionGuide → `mission_guide`로 테이블명 변경
- Collection: `memo` 길이 500으로 확장, `image_id→local_image_id`로 의미 명확화(프론트 IndexedDB의 로컬 이미지 참조), `status` 값 `SUCCESS/FAILURE`로 정리(`UNPROCESSED` 제거), `mission_id` UNIQUE 추가(Mission:Collection = 1:1), `updated_at` 추가
- Emotion: 변경 없음

### 확정이 필요한 값 (TODO)

아래 두 컬럼은 피드백에 값 목록이 명시되지 않아 우선 자유 문자열(VARCHAR)로 생성해뒀습니다. 팀 논의 후 값이 정해지면 마이그레이션을 추가해 반영해주세요.

- `trip.mood`: 값 목록 미정
- `mission.mission_category`: enum 값 목록 미정
