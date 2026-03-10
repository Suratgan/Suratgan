package com.suratgan.backend.order.presentation.dto;

import java.util.UUID;

public record OrderItemResponse(
    int menuId,
    String menuName,
    int menuPrice,
    int quantity
) {}