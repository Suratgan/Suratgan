package com.suratgan.backend.global.presentation;

import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** ✅ 우리가 의도한 비즈니스 예외 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException e, HttpServletRequest req) {
        ErrorCode ec = e.getErrorCode();

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ec.getStatus().value())
                .error(ec.getStatus().name())
                .code(ec.getCode())
                .message(ec.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(ec.getStatus()).body(body);
    }

    /** ✅ @Valid 검증 실패 예외(JSON 필드 누락/형식 오류 등) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {

        List<ApiErrorResponse.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> ApiErrorResponse.FieldError.builder()
                        .field(fe.getField())
                        .reason(fe.getDefaultMessage())
                        .build())
                .toList();

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("BAD_REQUEST")
                .code(ErrorCode.INVALID_REQUEST.getCode())
                .message("입력값 검증에 실패했습니다.")
                .path(req.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    /** ✅ 그 외 예외(최소한의 안전망) */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknown(Exception e, HttpServletRequest req) {

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("INTERNAL_SERVER_ERROR")
                .code("INTERNAL_ERROR")
                .message("서버 오류가 발생했습니다.")
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(500).body(body);
    }
}