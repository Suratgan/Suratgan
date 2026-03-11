package com.suratgan.backend.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserMeUpdateRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String nickname;

    private String phoneNumber;
}