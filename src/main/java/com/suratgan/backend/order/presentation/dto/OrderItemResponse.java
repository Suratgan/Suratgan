package com.suratgan.backend.order.presentation.dto;

import java.util.UUID;

public record OrderItemResponse(
    UUID menuId,
    String menuName,
    int menuPrice,
    int quantity
) {}