package com.suratgan.backend.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewContent {

    @Column(nullable = false)
    private String subject;

    @Lob    // 리뷰 내용은 길이가 길 수 있으므로 LOB으로 지정
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int score; // 평점

    @Builder
    protected ReviewContent(String subject, String content, int score) {
        this.subject = subject;
        this.content = content;

        setScore(score); // 평점 유효성 검사
    }

    private void setScore(int score) {
        // 리뷰의 평점은 필수이며 1~5점 사이 선택
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("평점은 1에서 5 사이의 값이어야 합니다.");
        }

        this.score = score;
    }
}
