package com.suratgan.backend.order.presentation;

import com.suratgan.backend.order.application.OrderService;
import com.suratgan.backend.order.application.dto.OrderServiceDto;
import com.suratgan.backend.order.application.query.OrderQueryService;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.presentation.dto.OrderResponse;
import com.suratgan.backend.order.presentation.dto.OrderSummaryResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    // 주문 생성
    @PostMapping("/orders")
    public OrderId createOrder(@RequestBody OrderServiceDto.Create request) {
        return orderService.createOrder(request);
    }

    // 고객 주문 목록 조회
    @GetMapping("/users/{userId}/orders")
    public List<OrderSummaryResponse> getMyOrders(@PathVariable UUID userId) {
        return orderQueryService.getUserOrders(userId);
    }

    // 고객 주문 상세 조회
    @GetMapping("/users/{userId}/orders/{orderId}")
    public OrderResponse getMyOrder(@PathVariable UUID userId, @PathVariable UUID orderId) {
        return orderQueryService.getUserOrder(userId, orderId);
    }

    // 매장 주문 목록 조회
    @GetMapping("/stores/{storeId}/orders")
    public List<OrderSummaryResponse> getStoreOrders(@PathVariable UUID storeId) {
        return orderQueryService.getStoreOrders(storeId);
    }

    // 매장 주문 상세 조회
    @GetMapping("/stores/{storeId}/orders/{orderId}")
    public OrderResponse getStoreOrder(@PathVariable UUID storeId, @PathVariable UUID orderId) {
        return orderQueryService.getStoreOrder(storeId, orderId);
    }
}
