package com.suratgan.backend.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponseDto {
    private String message;
}
