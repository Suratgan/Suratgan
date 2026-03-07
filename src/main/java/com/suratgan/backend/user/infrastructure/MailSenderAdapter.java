package com.suratgan.backend.user.infrastructure;

import com.suratgan.backend.user.domain.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderAdapter {

//    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    public void send(String to, String code) {
//     메일 전송 기능 로그로 인증코드 확인
        System.out.println("이메일 인증코드 발송(로그 대체)");
        System.out.println(" to  = " + to);
        System.out.println(" code = " + code);

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("회원가입 인증번호");
//        message.setText("인증번호는 : " + code);
//
//        mailSender.send(message);
    }

    public void verifyCode(String email, String code) {
        EmailVerification verification =
                emailVerificationRepository.findTopByEmailOrderByIdDesc(email)
                        .orElseThrow(() -> new IllegalArgumentException("인증번호 요청 없음"));
        if(!verification.getCode().equals(code)) {
            throw new IllegalArgumentException("인증번호 불일치");
        }

        verification.verify(code);
    }
}
