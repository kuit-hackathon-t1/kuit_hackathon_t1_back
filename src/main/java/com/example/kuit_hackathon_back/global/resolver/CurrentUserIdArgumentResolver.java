package com.example.kuit_hackathon_back.global.resolver;

import com.example.kuit_hackathon_back.global.exception.BusinessException;
import com.example.kuit_hackathon_back.global.exception.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_NAME = "X-User-Id";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class)
                && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String value = webRequest.getHeader(HEADER_NAME);
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "X-User-Id 헤더가 필요합니다.");
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "X-User-Id 헤더 형식이 올바르지 않습니다.");
        }
    }
}
