package com.suratgan.backend.user.application.dto;

import com.suratgan.backend.user.domain.Role;
import com.suratgan.backend.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserMeResponseDto {
    private UUID id;
    private String nickname;
    private String email;
    private Role role;
    private String phoneNumber;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
