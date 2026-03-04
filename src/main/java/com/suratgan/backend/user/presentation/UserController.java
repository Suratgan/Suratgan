package com.suratgan.backend.user.presentation;

import com.suratgan.backend.user.application.SignupService;
import com.suratgan.backend.user.application.dto.SignupRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody@Valid SignupRequestDto request) {
        signupService.signup(request);
        return ResponseEntity.ok("회원가입 완료");
    }
}
