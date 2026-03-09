package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCategory extends BaseEntity {
    @Column(length=45, name="category_id", nullable = false)
    private UUID categoryId;

    protected void remove() {
        deletedAt = LocalDateTime.now();
    }
}
