package com.example.kuit_hackathon_back.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.kuit_hackathon_back.global.resolver.CurrentUserIdArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserIdArgumentResolver());
    }

    /**
     * 프론트(Vercel 배포) 및 로컬 개발 환경에서의 API 호출을 허용한다.
     *
     * <p>허용 origin 목록은 {@link CorsProperties}(cors.allowed-origins 프로퍼티)에서 주입받는다. Vercel은 브랜치/커밋마다
     * 프리뷰 URL이 새로 생성될 수 있어서, 배포 환경에서는 CORS_ALLOWED_ORIGINS 환경변수로 필요한 도메인을 코드 수정 없이 추가/변경할 수 있다.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration mapping = registry.addMapping("/api/**");
        mapping.allowedOriginPatterns(corsProperties.getAllowedOrigins().toArray(new String[0]));
        mapping.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        mapping.allowedHeaders("*");
        mapping.allowCredentials(true);
        mapping.maxAge(3600);
    }
}
