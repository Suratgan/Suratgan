package com.suratgan.backend.user.application;

import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import com.suratgan.backend.user.domain.EmailVerification;
import com.suratgan.backend.user.infrastructure.EmailVerificationRepository;
import com.suratgan.backend.user.infrastructure.JpaUserRepository;
import com.suratgan.backend.user.infrastructure.MailSenderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationService {

    private final JpaUserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailSenderAdapter mailSenderAdapter;

    private static final int EXPIRE_MINUTES = 5;

    public void requestVerification(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String code = generate6DigitCode();

//      생성된 인증코드 및 전송될 이메일 로그
        System.out.println(" 생선된 인증코드 = " + code + "(email=" + email +")");
        //같은 이메일로 다시 요청하면 기존 것 삭제 또는 갱신
        //emailVerificationRepository.deleteByEmail(email);

        EmailVerification verification = EmailVerification.create(email, code, EXPIRE_MINUTES);
        emailVerificationRepository.save(verification);

        mailSenderAdapter.send(email, code);
    }

    private String generate6DigitCode() {
        SecureRandom random = new SecureRandom();
        String code = String.format("%06d", random.nextInt(1_000_000));
        return code;
    }

    public void verifyCode(String email, String code) {
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_NOT_FOUND));
        verification.verify(code);
    }
}
