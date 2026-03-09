package com.suratgan.backend.user.presentation;

import com.suratgan.backend.user.application.UserMeService;
import com.suratgan.backend.user.application.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/me")
public class UserMeCotroller {

    private final UserMeService userMeService;

    @GetMapping
    public ResponseEntity<UserMeResponseDto> getMe() {
        return ResponseEntity.ok(userMeService.getMe());
    }

    @PatchMapping
    public ResponseEntity<UserMeResponseDto> updateMe(@Valid @RequestBody UserMeUpdateRequestDto request) {
        return ResponseEntity.ok(userMeService.updateMe(request));
    }

    @DeleteMapping
    public ResponseEntity<DeleteMeResponseDto> delete() {
        userMeService.deleteMe();
        return ResponseEntity.ok(DeleteMeResponseDto.builder()
                        .message("회원 탈퇴가 완료되었습니다.")
                .build());
    }

    @PatchMapping("/password")
    public ResponseEntity<UserPasswordUpdateResponseDto> changePassword(
            @Valid @RequestBody UserPasswordUpdateRequestDto request
    ) {
        return ResponseEntity.ok(userMeService.changePassword(request));
    }
}
