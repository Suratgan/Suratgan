package com.suratgan.backend.review.application;

import com.suratgan.backend.global.domain.service.CustomerCheck;
import com.suratgan.backend.global.domain.service.ReviewerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.review.domain.Review;
import com.suratgan.backend.review.domain.ReviewId;
import com.suratgan.backend.review.domain.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RoleCheck roleCheck;
    private final ReviewerCheck reviewerCheck;
    private final CustomerCheck customerCheck;

    public ReviewId createReview(UUID orderId, String subject, String content, int score) {
        // 도메인(Review)을 생성하며 비즈니스 로직(권한 체크 등)을 수행합니다.
        Review review = Review.builder()
                .orderId(orderId)
                .subject(subject)
                .content(content)
                .score(score)
                .roleCheck(roleCheck)
                .reviewerCheck(reviewerCheck)
                .customerCheck(customerCheck)
                .build();

        return reviewRepository.save(review).getId(); // PostgreSQL 저장 [cite: 2026-02-24]
    }
}