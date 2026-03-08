package com.suratgan.backend.payment.infrastructure.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossCancelPayment {

    private final TossApiHelper tossApiHelper;

    public TossCancelResponse cancel(String paymentKey, String cancelReason) {
        TossCancelRequest request = TossCancelRequest.builder()
                .cancelReason(cancelReason)
                .build();

        return tossApiHelper.client()
                .post()
                .uri("/v1/payments/{paymentKey}/cancel", paymentKey)
                .body(request)
                .retrieve()
                .body(TossCancelResponse.class);
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossCancelRequest {
        private String cancelReason;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossCancelResponse {
        private String paymentKey;
        private String status;
        private String canceledAt;
    }
}