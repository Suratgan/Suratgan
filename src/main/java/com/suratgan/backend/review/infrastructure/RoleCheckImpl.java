package com.suratgan.backend.review.infrastructure;

import com.suratgan.backend.global.domain.service.RoleCheck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleCheckImpl implements RoleCheck {

    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public boolean hasRole(List<String> roles) {
        return true;
    }
}