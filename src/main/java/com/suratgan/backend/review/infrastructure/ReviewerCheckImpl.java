package com.suratgan.backend.review.infrastructure;

import com.suratgan.backend.global.domain.service.ReviewerCheck;
import com.suratgan.backend.review.domain.ReviewId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReviewerCheckImpl implements ReviewerCheck {

    @Override
    public boolean check(ReviewId reviewId, UUID orderId) {
        return true;
    }
}