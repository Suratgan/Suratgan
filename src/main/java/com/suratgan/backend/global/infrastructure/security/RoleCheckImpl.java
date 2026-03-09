package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.user.application.UserMeService;
import com.suratgan.backend.user.application.dto.UserMeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleCheckImpl implements RoleCheck {
    private final UserMeService userMeService;

    @Override
    public boolean hasRole(String role) {
        return StringUtils.hasText(role) && hasRole(List.of(role));
    }

    @Override
    public boolean hasRole(List<String> roles) {
        UserMeResponseDto me = userMeService.getMe();

        return roles != null && roles.stream().anyMatch(role -> role.equals(me.getRole().name()));
    }
}
