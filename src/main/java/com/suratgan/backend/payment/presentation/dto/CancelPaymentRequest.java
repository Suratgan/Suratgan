package com.suratgan.backend.payment.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CancelPaymentRequest {
    private String cancelReason;
}