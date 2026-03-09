package com.suratgan.backend.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {
    @Column(nullable = false)
    private UUID userId;

    @Column(length = 20, nullable = false)
    private String role;

    @Column(length = 200, nullable = false)
    private String name;

    @Builder
    protected Owner(UUID id, String role, String name) {
        this.userId = id;
        this.role = role;
        this.name = name;
    }
}
