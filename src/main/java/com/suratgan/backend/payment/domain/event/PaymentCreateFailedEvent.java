package com.suratgan.backend.payment.domain.event;

import java.util.UUID;

public record PaymentCreateFailedEvent(
    UUID orderId
) {}