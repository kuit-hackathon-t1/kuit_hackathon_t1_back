package com.example.kuit_hackathon_back.domain.mission.ai;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.kuit_hackathon_back.domain.trip.entity.CompanionType;
import com.example.kuit_hackathon_back.domain.trip.entity.TripMood;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gemini(Google Generative Language API) 기반 미션 배치 생성기.
 *
 * <p>다른 AI 제공자로 교체하려면 {@link MissionAiClient}를 구현하는 새 클래스를 추가하고 빈만 바꾸면 된다.
 */
@Component
public class GeminiMissionAiClient implements MissionAiClient {

    private static final List<String> MISSION_CATEGORIES =
            List.of("OBSERVATION", "ACTION", "LOCAL", "RANDOM");

    private final GeminiProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient;

    public GeminiMissionAiClient(GeminiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    @Override
    public List<GeneratedMission> generateMissions(MissionAiRequest request) {
        if (!properties.isConfigured()) {
            throw new MissionAiGenerationException("GEMINI_API_KEY가 설정되지 않았습니다.");
        }

        String responseText;
        try {
            String baseUrl = properties.getBaseUrl();
            String model = properties.getModel();
            String apiKey = properties.getApiKey();
            String url = "%s/models/%s:generateContent?key=%s".formatted(baseUrl, model, apiKey);

            Map<String, Object> response =
                    restClient
                            .post()
                            .uri(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(buildRequestBody(request))
                            .retrieve()
                            .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            responseText = extractText(response);
        } catch (RestClientException e) {
            throw new MissionAiGenerationException("Gemini API 호출에 실패했습니다.", e);
        }

        return parseMissions(responseText);
    }

    private Map<String, Object> buildRequestBody(MissionAiRequest request) {
        Map<String, Object> userContent =
                Map.of("role", "user", "parts", List.of(Map.of("text", buildPrompt(request))));
        Map<String, Object> generationConfig =
                Map.of(
                        "responseMimeType",
                        "application/json",
                        "responseSchema",
                        buildResponseSchema(),
                        "temperature",
                        1.0);
        return Map.of("contents", List.of(userContent), "generationConfig", generationConfig);
    }

    private String buildPrompt(MissionAiRequest request) {
        String mood = request.mood() == null ? "미정" : describeMood(request.mood());
        String companion =
                request.companionType() == null ? "미정" : describeCompanion(request.companionType());

        return """
                너는 여행 미션 추천 앱 "청춘도감"의 미션 생성기다.
                아래 여행 정보에 어울리는, 서로 다른 미션을 정확히 %d개 만들어라.

                [여행 정보]
                - 여행 이름: %s
                - 지역: %s
                - 동행 유형: %s
                - 여행 분위기: %s

                [작성 규칙]
                - 각 미션은 title(15자 내외), description(1~2문장), missionCategory, isLocal, guides(질문 3개)로 구성한다.
                - missionCategory는 다음 중 하나를 그대로 사용한다.
                  OBSERVATION(관찰형: 주변을 유심히 관찰), ACTION(행동형: 직접 행동해보기),
                  LOCAL(지역형: 이 지역만의 특색을 반영), RANDOM(랜덤: 예상 못한 우연성).
                - isLocal은 이 지역이 아니면 하기 어려운 미션이면 true, 어느 지역에서든 할 수 있으면 false.
                - guides는 미션 수행 후 회고를 돕는 짧은 질문 3개.
                - 여행 분위기(%s) 톤에 맞춰 문장을 써라.
                - 동행 유형(%s)을 고려해서, 혼자 하기 좋은 미션과 함께 하기 좋은 미션을 적절히 섞어라.
                - %d개 모두 서로 겹치지 않는 내용으로 만든다.
                - 반드시 JSON 배열만 출력한다. 다른 설명 문장은 붙이지 않는다.
                """
                .formatted(
                        request.count(),
                        request.tripName(),
                        request.region(),
                        companion,
                        mood,
                        mood,
                        companion,
                        request.count());
    }

    private String describeMood(TripMood mood) {
        return switch (mood) {
            case EMOTIONAL -> "EMOTIONAL(감성 남기기 - 잔잔하고 감성적으로)";
            case WANDERING -> "WANDERING(헤매기 - 목적 없이 배회하는 느낌으로)";
            case LOCAL -> "LOCAL(지역 느끼기 - 지역색이 짙게)";
            case COURAGE -> "COURAGE(조금 용기내기 - 약간의 도전/용기가 필요하게)";
        };
    }

    private String describeCompanion(CompanionType companionType) {
        return switch (companionType) {
            case ALONE -> "ALONE(혼자)";
            case FRIEND -> "FRIEND(친구)";
            case COUPLE -> "COUPLE(연인)";
            case FAMILY -> "FAMILY(가족)";
        };
    }

    private Map<String, Object> buildResponseSchema() {
        Map<String, Object> titleSchema = Map.of("type", "STRING");
        Map<String, Object> descriptionSchema = Map.of("type", "STRING");
        Map<String, Object> missionCategorySchema =
                Map.of("type", "STRING", "enum", MISSION_CATEGORIES);
        Map<String, Object> isLocalSchema = Map.of("type", "BOOLEAN");
        Map<String, Object> guidesSchema =
                Map.of("type", "ARRAY", "items", Map.of("type", "STRING"));

        Map<String, Object> properties =
                Map.of(
                        "title",
                        titleSchema,
                        "description",
                        descriptionSchema,
                        "missionCategory",
                        missionCategorySchema,
                        "isLocal",
                        isLocalSchema,
                        "guides",
                        guidesSchema);

        List<String> requiredFields =
                List.of("title", "description", "missionCategory", "isLocal", "guides");

        Map<String, Object> itemSchema =
                Map.of("type", "OBJECT", "properties", properties, "required", requiredFields);

        return Map.of("type", "ARRAY", "items", itemSchema);
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (RuntimeException e) {
            throw new MissionAiGenerationException("Gemini 응답 형식이 예상과 다릅니다.", e);
        }
    }

    private List<GeneratedMission> parseMissions(String json) {
        try {
            List<GeneratedMission> missions =
                    objectMapper.readValue(json, new TypeReference<List<GeneratedMission>>() {});
            if (missions.isEmpty()) {
                throw new MissionAiGenerationException("AI가 빈 미션 목록을 반환했습니다.");
            }
            return missions;
        } catch (JsonProcessingException e) {
            throw new MissionAiGenerationException("AI 응답을 파싱하지 못했습니다.", e);
        }
    }
}
