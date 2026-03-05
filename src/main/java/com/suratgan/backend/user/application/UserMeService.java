package com.suratgan.backend.user.application;

import com.suratgan.backend.user.application.dto.UserMeResponseDto;
import com.suratgan.backend.user.domain.User;
import com.suratgan.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMeService {

    private final UserRepository userRepository;

    public UserMeResponseDto getMe(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        if(user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 사용자입니다.");
        }

        return UserMeResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .isDeleted(user.isDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }
}
