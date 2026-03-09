package com.suratgan.backend.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String tokenType; // Bearer type
    private long expiresInSeconds;
}
