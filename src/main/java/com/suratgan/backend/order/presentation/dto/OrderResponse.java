package com.suratgan.backend.order.presentation.dto;

import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID orderId,
    UUID userId,
    String ordererName,
    String ordererMobile,
    String ordererEmail,
    String storeName,
    String storeAddress,
//    String storeTel,
    int totalAmount,
    List<OrderItemResponse> items
) {}