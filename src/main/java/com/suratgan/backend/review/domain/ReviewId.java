package com.suratgan.backend.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewId {

    @Column(name = "review_id")
    private UUID id;

    public static ReviewId of() {
        return ReviewId.of(UUID.randomUUID());
    }

    public static ReviewId of(UUID id) {
        return new ReviewId(id);
    }
}
