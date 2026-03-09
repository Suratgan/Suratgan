package com.suratgan.backend.review.domain;

import com.suratgan.backend.global.domain.service.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reviewer {

    @Column(name = "reviewer_id")
    private UUID id;

    @Column(length = 45)
    private String reviewerName;

    protected Reviewer(UserDetails userDetails) {
        if (userDetails == null || userDetails.getId() == null) {
            throw new IllegalArgumentException("유효하지 않은 리뷰 작성자입니다. 다시 로그인해 주세요.");
        }

        this.id = userDetails.getId();
        this.reviewerName = userDetails.getName();
    }
}
