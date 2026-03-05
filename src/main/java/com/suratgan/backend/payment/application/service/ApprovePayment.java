package com.suratgan.backend.payment.application.service;

import com.suratgan.backend.payment.application.dto.ApprovePaymentResult;

public interface ApprovePayment {

    ApprovePaymentResult approve(String orderIdForToss, String paymentKey, long amount);
}