package com.suratgan.backend.payment.infrastructure.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossApprovePayment {

    private final TossApiHelper tossApiHelper;

    public TossApproveResponse approve(String paymentKey, String orderId, long amount) {
        TossApproveRequest request = TossApproveRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(amount)
                .build();

        return tossApiHelper.client()
                .post()
                .uri("/v1/payments/confirm")
                .body(request)
                .retrieve()
                .body(TossApproveResponse.class);
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossApproveRequest {
        private String paymentKey;
        private String orderId;
        private long amount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossApproveResponse {
        private String paymentKey;
        private String orderId;
        private String status;
        private long totalAmount;
        private String approvedAt;
    }
}