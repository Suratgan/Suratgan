package com.suratgan.backend.user.presentation;

import com.suratgan.backend.user.application.SignupService;
import com.suratgan.backend.user.application.dto.NicknameCheckRequestDto;
import com.suratgan.backend.user.application.dto.NicknameCheckResponseDto;
import com.suratgan.backend.user.application.dto.SignupRequestDto;
import com.suratgan.backend.user.application.dto.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SignupService signupService;
    private final UserQueryService userQueryService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody@Valid SignupRequestDto request) {
        signupService.signup(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/nickname-check")
    public ResponseEntity<NicknameCheckResponseDto> verify(@RequestBody @Valid NicknameCheckRequestDto request) {
        return ResponseEntity.ok(userQueryService.checkNickname(request.getNickname()));
    }
}
