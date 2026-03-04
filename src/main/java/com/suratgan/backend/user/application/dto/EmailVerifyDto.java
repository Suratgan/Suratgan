package com.suratgan.backend.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailVerifyDto {
    @Email @NotBlank
    private String email;

    @NotBlank
    private String code;
}
