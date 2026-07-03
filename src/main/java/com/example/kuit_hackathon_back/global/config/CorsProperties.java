package com.example.kuit_hackathon_back.global.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * CORS로 허용할 프론트 origin 목록 설정. application-local.yml 등에서 아래 키로 주입한다.
 *
 * <pre>
 * cors:
 *   allowed-origins: ${CORS_ALLOWED_ORIGINS:http://localhost:3000,http://localhost:5173}
 * </pre>
 *
 * <p>콤마로 구분된 문자열을 Spring이 자동으로 List&lt;String&gt;으로 바인딩해준다. 배포 환경에서 프론트 도메인이 추가/변경되면 코드 수정 없이
 * Render 환경변수(CORS_ALLOWED_ORIGINS)만 바꾸면 된다.
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private List<String> allowedOrigins = List.of();
}
