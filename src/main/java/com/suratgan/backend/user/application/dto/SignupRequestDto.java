package com.suratgan.backend.user.application.dto;

import com.suratgan.backend.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
public class SignupRequestDto {

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotNull
    private Role role;
}
