package com.suratgan.backend.payment.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ApprovePaymentResult {

    private UUID paymentId;
    private UUID orderId;

    private String paymentKey;
    private Integer amount;
    private String status;
    private LocalDateTime approvedAt;
}