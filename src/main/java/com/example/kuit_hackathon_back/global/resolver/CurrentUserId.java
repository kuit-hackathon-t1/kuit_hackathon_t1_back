package com.example.kuit_hackathon_back.global.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * X-User-Id 헤더 값을 컨트롤러 파라미터에 주입한다. (Long userId)
 *
 * <p>{@code @Parameter(hidden = true)}로 Swagger 자동 생성 파라미터를 숨긴다. 실제 X-User-Id 헤더 문서는 {@link
 * com.example.kuit_hackathon_back.global.config.SwaggerUserIdCustomizer}가 별도로 추가한다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface CurrentUserId {}
