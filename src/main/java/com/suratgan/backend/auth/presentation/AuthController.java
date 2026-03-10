package com.suratgan.backend.auth.presentation;

import com.suratgan.backend.auth.application.AuthService;
import com.suratgan.backend.auth.application.dto.LoginRequestDto;
import com.suratgan.backend.auth.application.dto.LoginResponseDto;
import com.suratgan.backend.auth.application.dto.LogoutResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody@Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return ResponseEntity.ok(authService.logout(authorizationHeader));
    }
}
