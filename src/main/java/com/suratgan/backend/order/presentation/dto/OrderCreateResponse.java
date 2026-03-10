package com.suratgan.backend.order.presentation.dto;

import java.util.UUID;

public record OrderCreateResponse(
        UUID orderId,
        int amount,
        String storeName
) {}