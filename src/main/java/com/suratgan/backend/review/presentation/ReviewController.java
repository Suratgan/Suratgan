package com.suratgan.backend.review.presentation;

import com.suratgan.backend.global.domain.service.UserDetails;
import com.suratgan.backend.review.application.ReviewService;
import com.suratgan.backend.review.domain.ReviewId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// DTO는 presentation 패키지 내부에 두는 것이 일반적입니다.
record ReviewRequest(UUID orderId, String subject, String content, int score) {}

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewId> createReview(
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ReviewId reviewId = reviewService.createReview(
                request.orderId(), request.subject(), request.content(), request.score(), userDetails
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }
}