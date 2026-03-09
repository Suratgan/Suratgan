package com.suratgan.backend.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
