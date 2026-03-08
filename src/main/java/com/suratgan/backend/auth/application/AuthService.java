package com.suratgan.backend.auth.application;

import com.suratgan.backend.auth.application.dto.LoginRequestDto;
import com.suratgan.backend.auth.application.dto.LoginResponseDto;
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

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalStateException("해당 닉네임은 존재하지 않습니다."));
        if (user.isDeleted()){
            throw new IllegalStateException("탈퇴한 사용자입니다.");
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());

        return LoginResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtTokenProvider.getAccessTokenExpiration()/1000)
                .build();
    }
}
