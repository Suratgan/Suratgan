package com.suratgan.backend.payment.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApprovePaymentRequest {
    private String orderId;     // 토스 orderId
    private String paymentKey;  // 토스 paymentKey
    private long amount;        // 승인 금액
}