package com.suratgan.backend.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryId {
    @Column(length = 45)
    private UUID id;

    public static CategoryId of() {
        return CategoryId.of(UUID.randomUUID());
    }

    public static CategoryId of(UUID id) {
        return new CategoryId(id);
    }
}
