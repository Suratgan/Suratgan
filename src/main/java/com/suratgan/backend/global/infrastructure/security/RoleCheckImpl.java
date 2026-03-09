package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.RoleCheck;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleCheckImpl implements RoleCheck {

    @Override
    public boolean hasRole(String role) {
        return getAuthorities().contains(role);
    }

    @Override
    public boolean hasRole(List<String> roles) {
        Collection<String> userAuthorities = getAuthorities();
        return roles.stream().anyMatch(userAuthorities::contains);
    }

    // 사용자의 권한 목록 반환
    private Collection<String> getAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            return List.of();
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
