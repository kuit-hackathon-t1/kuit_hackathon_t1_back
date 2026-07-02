package com.example.kuit_hackathon_back.global.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** X-User-Id 헤더 값을 컨트롤러 파라미터에 주입한다. (Long userId) */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserId {}
