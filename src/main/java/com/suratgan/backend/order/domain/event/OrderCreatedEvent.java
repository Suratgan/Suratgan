package com.suratgan.backend.order.domain.event;

import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId,
    int amount
) {}
