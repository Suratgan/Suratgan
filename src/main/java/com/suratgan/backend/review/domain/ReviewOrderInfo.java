package com.suratgan.backend.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewOrderInfo {

    @Column(nullable = false)
    private UUID orderId;       // 주문번호

    private UUID storeId;       // 상점 ID
    private String storeName;   // 상점명

    // TODO: ReviewOrderItem은 Store 코드 들어온 후 추가 예정

    @Builder
    protected ReviewOrderInfo(UUID orderId, UUID storeId, String storeName) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.storeName = storeName;
    }
}