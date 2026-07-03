package com.example.kuit_hackathon_back.domain.mission.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Gemini API 연동 설정. application-local.yml 등에서 아래 키로 주입한다.
 *
 * <pre>
 * gemini:
 *   api-key: ${GEMINI_API_KEY:}
 *   model: gemini-2.5-flash
 * </pre>
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties {

    /** Google AI Studio에서 발급받은 API 키. 절대 코드/문서에 실제 값을 커밋하지 않는다. */
    private String apiKey;

    /** gemini-2.0-flash는 2026-06-01부로 종료(shutdown)된 모델이라 기본값을 gemini-2.5-flash로 둔다. */
    private String model = "gemini-2.5-flash";

    private String baseUrl = "https://generativelanguage.googleapis.com/v1beta";

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}
