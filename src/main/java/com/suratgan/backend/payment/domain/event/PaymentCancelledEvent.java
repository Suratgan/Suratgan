package com.suratgan.backend.payment.domain.event;

import java.util.UUID;

public record PaymentCancelledEvent(
    UUID orderId
) {}