package com.suratgan.backend.user.application;


import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import com.suratgan.backend.user.application.dto.SignupRequestDto;
import com.suratgan.backend.user.domain.User;
import com.suratgan.backend.user.domain.UserRepository;
import com.suratgan.backend.user.infrastructure.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    public void signup(SignupRequestDto request) {

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        boolean verified = emailVerificationRepository.existsByEmailAndVerifiedTrue(request.getEmail());

        if(!verified) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.create(
                request.getNickname(),
                encodedPassword,
                request.getEmail(),
                request.getRole()
        );

        userRepository.save(user);
    }
}
