package com.suratgan.backend.auth.infrastructure.jwt;

import com.suratgan.backend.user.domain.Role;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenMillis;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.access-token-exp-minutes}") long expMinutes)
    {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenMillis = expMinutes * 60 * 1000L;
    }

    public String createAccessToken(UUID userId, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenMillis);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public long getAccessTokenExpiration() {
        return accessTokenMillis;
    }
}
