package com.example.kuit_hackathon_back.global.config;

import java.util.Arrays;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.example.kuit_hackathon_back.global.resolver.CurrentUserId;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * @CurrentUserId 파라미터가 있는 API는 Swagger UI에서 X-User-Id 헤더 입력창이 보이도록 자동으로 추가해준다.
 */
@Component
public class SwaggerUserIdCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        boolean requiresUserId =
                Arrays.stream(handlerMethod.getMethodParameters())
                        .anyMatch(
                                parameter -> parameter.hasParameterAnnotation(CurrentUserId.class));

        if (requiresUserId) {
            operation.addParametersItem(
                    new Parameter()
                            .in("header")
                            .name("X-User-Id")
                            .description("로그인으로 발급받은 사용자 ID")
                            .required(true)
                            .schema(new Schema<Long>().type("integer").format("int64")));
        }
        return operation;
    }
}
