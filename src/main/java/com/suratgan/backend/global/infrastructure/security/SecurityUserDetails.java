package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.UserDetails;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserDetails implements UserDetails {

    @Override
    public UUID getId() {
        return UUID.randomUUID(); // 임시
    }

    @Override
    public String getName() {
        return "TEST_USER";
    }

    @Override
    public String getEmail() {
        return "test@test.com";
    }

    @Override
    public String getMobile() {
        return "01000000000";
    }

    @Override
    public String getRole() {
        return "";
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
