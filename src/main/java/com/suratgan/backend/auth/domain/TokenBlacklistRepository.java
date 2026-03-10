package com.suratgan.backend.auth.domain;

import java.time.Duration;

public interface TokenBlacklistRepository {
    void blacklist(String token, Duration ttl);
    boolean isBlacklisted(String token);
}
