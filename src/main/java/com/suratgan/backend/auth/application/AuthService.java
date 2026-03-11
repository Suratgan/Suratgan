package com.suratgan.backend.auth.application;

import com.suratgan.backend.auth.application.dto.LoginRequestDto;
import com.suratgan.backend.auth.application.dto.LoginResponseDto;
import com.suratgan.backend.auth.application.dto.LogoutResponseDto;
import com.suratgan.backend.auth.domain.TokenBlacklistRepository;
import com.suratgan.backend.auth.infrastructure.jwt.JwtTokenProvider;
import com.suratgan.backend.user.domain.User;
import com.suratgan.backend.user.infrastructure.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JpaUserRepository userRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalStateException("해당 닉네임은 존재하지 않습니다."));
        if (user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 사용자입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createAccessToken(user.getNickname(), user.getRole());

        return LoginResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtTokenProvider.getAccessTokenExpiration() / 1000)
                .build();
    }

    @Transactional
    public LogoutResponseDto logout(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 올바르지 않습니다.");
        }

        String token = bearerToken.substring(7);

        if (!jwtTokenProvider.validate(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }

        long remainingMillis = jwtTokenProvider.getRemainingMillis(token);

        if (remainingMillis > 0) {
            tokenBlacklistRepository.blacklist(token, java.time.Duration.ofMillis(remainingMillis));
        }

        return LogoutResponseDto.builder()
                .message("로그아웃 되었습니다.")
                .build();
    }
}
