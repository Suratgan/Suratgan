package com.suratgan.backend.payment.domain;

import lombok.Getter;
import java.util.UUID;

@Getter
public class PaymentApprovedEvent {
    private final UUID orderId;

    public PaymentApprovedEvent(UUID orderId) {
        this.orderId = orderId;
    }
}