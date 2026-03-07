package com.suratgan.backend.global.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;     // "BAD_REQUEST"
    private final String code;      // "EMAIL_NOT_VERIFIED" 같은 내부 코드
    private final String message;   // 사용자에게 보여줄 메시지
    private final String path;      // 요청 경로

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<FieldError> fieldErrors;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String reason;
    }
}