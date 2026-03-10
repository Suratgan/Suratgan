package com.suratgan.backend.auth.infrastructure.jwt;

import com.suratgan.backend.user.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
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
                            @Value("${jwt.access-token-exp-minutes}") long expMinutes) {
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

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getUserID(String token) {
        Claims claims = parseClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    public Role getRole(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get("role", String.class);
        return Role.valueOf(role);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public long getRemainingMillis(String token) {
        Date expiration = getExpiration(token);
        long remain = expiration.getTime() - System.currentTimeMillis();
        return Math.max(remain, 0);
    }
}
