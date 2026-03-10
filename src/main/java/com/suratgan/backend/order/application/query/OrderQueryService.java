package com.suratgan.backend.order.application.query;

import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.infrastructure.OrderRepository;
import com.suratgan.backend.order.presentation.dto.OrderItemResponse;
import com.suratgan.backend.order.presentation.dto.OrderResponse;
import com.suratgan.backend.order.presentation.dto.OrderSummaryResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    private Order findOrder(UUID orderId) {
        return orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다."));
    }

    // 고객 주문 목록 조회
    public List<OrderSummaryResponse> getUserOrders(UUID userId) {

        List<Order> orders = orderRepository.findByOrderer_Id(userId);

        return orders.stream()
            .map(order -> new OrderSummaryResponse(
                order.getId().getId(),
                order.getStoreInfo().getStoreName(),
                order.getTotalOrderPrice().getValue(),
                order.getStatus().name(),
                order.getCreatedAt()
            ))
            .toList();
    }

    // 고객 주문 상세 조회
    public OrderResponse getUserOrder(UUID userId, UUID orderId) {

        Order order = findOrder(orderId);
//        // TODO: 인증 연동 후 권한 체크 활성화
//        if (!order.getOrderer().getId().equals(userId)) {
//            throw new AccessDeniedException("조회 권한 없음");
//        }

        List<OrderItemResponse> items = order.getOrderItems()
            .stream()
            .map(i -> new OrderItemResponse(
                i.getItem().getMenuId(),
                i.getItem().getName(),
                i.getItem().getPrice().getValue(),
                i.getQuantity()
            ))
            .toList();

        return new OrderResponse(
            order.getId().getId(),
            order.getOrderer().getId(),
            order.getOrderer().getName(),
            order.getOrderer().getMobile(),
            order.getOrderer().getEmail(),
            order.getStoreInfo().getStoreName(),
            order.getStoreInfo().getStoreAddress(),
            order.getTotalOrderPrice().getValue(),
            items
        );
    }

    // 매장 주문 목록 조회
    public List<OrderSummaryResponse> getStoreOrders(UUID storeId) {

        List<Order> orders = orderRepository.findByStoreInfoStoreId(storeId);

        return orders.stream()
            .map(order -> new OrderSummaryResponse(
                order.getId().getId(),
                order.getStoreInfo().getStoreName(),
                order.getTotalOrderPrice().getValue(),
                order.getStatus().name(),
                order.getCreatedAt()
            ))
            .toList();
    }

    // 매장 주문 상세 조회
    public OrderResponse getStoreOrder(UUID storeId, UUID orderId) {

        Order order = findOrder(orderId);

//        // TODO: 인증 연동 후 권한 체크 활성화
//        if (!order.getStoreInfo().getStoreId().equals(storeId)) {
//            throw new AccessDeniedException("조회 권한 없음");
//        }

        List<OrderItemResponse> items = order.getOrderItems()
            .stream()
            .map(i -> new OrderItemResponse(
                i.getItem().getMenuId(),
                i.getItem().getName(),
                i.getItem().getPrice().getValue(),
                i.getQuantity()
            ))
            .toList();

        return new OrderResponse(
            order.getId().getId(),
            order.getOrderer().getId(),
            order.getOrderer().getName(),
            order.getOrderer().getMobile(),
            order.getOrderer().getEmail(),
            order.getStoreInfo().getStoreName(),
            order.getStoreInfo().getStoreAddress(),
//            order.getStoreInfo().getStoreTel(),
            order.getTotalOrderPrice().getValue(),
            items
        );
    }

}
