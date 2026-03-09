package com.suratgan.backend.order.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSummaryResponse {

    private UUID orderId;
    private String storeName;
    private int totalAmount;
    private String status;
    private LocalDateTime createdAt;

}