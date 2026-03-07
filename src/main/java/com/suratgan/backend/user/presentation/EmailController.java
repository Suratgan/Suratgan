package com.suratgan.backend.user.presentation;

import com.suratgan.backend.user.application.EmailVerificationService;
import com.suratgan.backend.user.application.dto.EmailRequestDto;
import com.suratgan.backend.user.application.dto.EmailVerifyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/request")
    public ResponseEntity<String> request(@RequestBody EmailRequestDto request) {
        emailVerificationService.requestVerification(request.getEmail());
        return ResponseEntity.ok("인증번호 발송 완료");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody EmailVerifyDto request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok("이메일 인증 완료");
    }
}
