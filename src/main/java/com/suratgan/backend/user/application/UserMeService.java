package com.suratgan.backend.user.application;

import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import com.suratgan.backend.global.infrastructure.security.SecurityUtils;
import com.suratgan.backend.user.application.dto.UserMeResponseDto;
import com.suratgan.backend.user.application.dto.UserMeUpdateRequestDto;
import com.suratgan.backend.user.application.dto.UserPasswordUpdateRequestDto;
import com.suratgan.backend.user.application.dto.UserPasswordUpdateResponseDto;
import com.suratgan.backend.user.domain.User;
import com.suratgan.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserMeResponseDto getMe() {
        UUID userId = SecurityUtils.currentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new BusinessException(ErrorCode.USER_DELETED);
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

    @Transactional
    public UserMeResponseDto updateMe(UserMeUpdateRequestDto request) {
        UUID userId = SecurityUtils.currentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if(user.isDeleted()) throw new BusinessException(ErrorCode.USER_DELETED);

        if(!user.getNickname().equals(request.getNickname())
        && userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        user.changeNickname(request.getNickname());

        userRepository.save(user);

        return UserMeResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteMe() {
        UUID userId = SecurityUtils.currentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();
        userRepository.save(user);
    }

    @Transactional
    public UserPasswordUpdateResponseDto changePassword(UserPasswordUpdateRequestDto request) {
        UUID userId = SecurityUtils.currentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new BusinessException(ErrorCode.USER_DELETED);
        }

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.changePassword(encodedNewPassword);

        userRepository.save(user);

        return UserPasswordUpdateResponseDto.builder()
                .message("비밀번호가 변경되었습니다.")
                .build();
    }
}
