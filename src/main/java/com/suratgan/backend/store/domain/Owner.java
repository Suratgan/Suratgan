package com.suratgan.backend.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {
    @Column(nullable = false)
    private Long id;

    @Column(length = 20, nullable = false)
    private String role;

    @Column(length = 200, nullable = false)
    private String name;

    @Builder
    protected Owner(Long id, String role, String name) {
        this.id = id;
        this.role = role;
        this.name = name;
    }
}
