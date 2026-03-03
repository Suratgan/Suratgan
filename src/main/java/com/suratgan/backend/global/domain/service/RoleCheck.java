package com.suratgan.backend.global.domain.service;

import java.util.List;

public interface RoleCheck {
    // 단일 RoleCheck
    boolean hasRole(String role);

    // List의 권한 중 해당하는 권한이 있는지 체크
    boolean hasRole(List<String> roles);
}
