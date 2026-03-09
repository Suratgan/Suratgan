package com.suratgan.backend.global.domain.service;

import java.util.UUID;

public interface UserDetails {
    UUID getId();
    String getName();
    String getRole();
    boolean isAuthenticated();
}
