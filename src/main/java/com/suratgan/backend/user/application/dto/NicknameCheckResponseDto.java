package com.suratgan.backend.user.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NicknameCheckResponseDto {
    private String nickname;
    private boolean available;
    private String message;
}
