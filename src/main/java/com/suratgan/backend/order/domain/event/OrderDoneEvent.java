package com.suratgan.backend.order.domain.event;

import java.util.UUID;

public record OrderDoneEvent(
    UUID orderId
) {}
