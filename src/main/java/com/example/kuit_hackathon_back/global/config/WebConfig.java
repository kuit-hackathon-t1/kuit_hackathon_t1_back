package com.example.kuit_hackathon_back.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.kuit_hackathon_back.global.resolver.CurrentUserIdArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserIdArgumentResolver());
    }

    /**
     * 프론트(Vercel 배포) 및 로컬 개발 환경에서의 API 호출을 허용한다.
     *
     * <p>Vercel은 브랜치/커밋마다 프리뷰 URL이 새로 생성될 수 있어서, 같은 프로젝트에서 나오는 모든 프리뷰 도메인을 패턴으로 허용해둔다. 정식 커스텀 도메인이
     * 나오면 그 도메인도 여기에 추가해야 한다.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration mapping = registry.addMapping("/api/**");
        mapping.allowedOriginPatterns(
                "https://kuit-hackathon-t1-front.vercel.app",
                "https://kuit-hackathon-t1-front-*-leedongkyus-projects-c6361242.vercel.app",
                "http://localhost:3000",
                "http://localhost:5173");
        mapping.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        mapping.allowedHeaders("*");
        mapping.allowCredentials(true);
        mapping.maxAge(3600);
    }
}
