package com.suratgan.backend.payment.application.service;

import com.suratgan.backend.payment.application.dto.CancelPaymentResult;

import java.util.UUID;

public interface CancelPayment {
    CancelPaymentResult cancel(UUID paymentId, String cancelReason);
}