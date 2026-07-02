package com.example.kuit_hackathon_back.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("KUIT Hackathon T1 Backend API")
                                .description(
                                        "여행 미션 채집 서비스 API 문서. "
                                                + "X-User-Id 헤더가 필요한 API는 /login으로 발급받은 userId를 넣어 테스트하세요.")
                                .version("v1"));
    }
}
