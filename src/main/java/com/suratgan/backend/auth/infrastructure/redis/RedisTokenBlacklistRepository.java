package com.suratgan.backend.auth.infrastructure.redis;

import com.suratgan.backend.auth.domain.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisTokenBlacklistRepository implements TokenBlacklistRepository {
    private static final String PREFIX = "blacklist";
    private final StringRedisTemplate redisTemplate;

    @Override
    public void blacklist(String token, Duration ttl) {
        redisTemplate.opsForValue().set(PREFIX + token, "logout", ttl);
    }

    @Override
    public boolean isBlacklisted(String token) {
        Boolean exists = redisTemplate.hasKey(PREFIX + token);
        return Boolean.TRUE.equals(exists);
    }
}
