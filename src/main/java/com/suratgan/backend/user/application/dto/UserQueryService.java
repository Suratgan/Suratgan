package com.suratgan.backend.user.application.dto;

import com.suratgan.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public NicknameCheckResponseDto checkNickname(String nickname) {
        String trimmedNickname = nickname.trim();

        boolean exists = userRepository.existsByNickname(trimmedNickname);

        return NicknameCheckResponseDto.builder()
                .nickname(trimmedNickname)
                .available(!exists)
                .message(exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.")
                .build();
    }
}
