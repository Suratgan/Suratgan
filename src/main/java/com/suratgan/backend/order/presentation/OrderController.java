package com.suratgan.backend.order.presentation;

import com.suratgan.backend.global.domain.service.UserDetails;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
    public OrderId createOrder(@RequestBody OrderServiceDto.Create request, UserDetails userDetails) {
        return orderService.createOrder(request, userDetails);
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

    // 주문 상태 변경(주문 접수)
    @PatchMapping("/orders/{orderId}/accept")
    public void acceptOrder(@PathVariable UUID orderId) {
        orderService.acceptOrder(orderId);
    }

    // 주문 상태 변경(배송 시작)
    @PatchMapping("/orders/{orderId}/delivery")
    public void startDelivery(@PathVariable UUID orderId) {
        orderService.startDelivery(orderId);
    }

    // 주문 상태 변경(주문 완료)
    @PatchMapping("/orders/{orderId}/done")
    public void doneOrder(@PathVariable UUID orderId) {
        orderService.completeOrder(orderId);
    }


    // 주문 취소
    @PatchMapping("/orders/{orderId}/cancel")
    public void cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
    }

    // 주문 상태 변경(배송 완료)
    @PatchMapping("/orders/{orderId}/delivery-done")
    public void deliveryDone(@PathVariable UUID orderId) {
        orderService.deliveryDone(orderId);
    }

    // 주문 준비 중
    @PatchMapping("/orders/{orderId}/preparing")
    public void preparing(@PathVariable UUID orderId) {
        orderService.preparing(orderId);
    }
}
