package com.suratgan.backend.user.presentation;

import com.suratgan.backend.user.application.UserMeService;
import com.suratgan.backend.user.application.dto.UserMeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserMeCotroller {

    private final UserMeService userMeService;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDto> me(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userMeService.getMe(userId));
    }
}
