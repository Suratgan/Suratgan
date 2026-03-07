package com.suratgan.backend.order.application;

import com.suratgan.backend.order.application.dto.OrderServiceDto;
import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.domain.OrderItem;
import com.suratgan.backend.order.domain.Orderer;
import com.suratgan.backend.order.domain.ProductInfo;
import com.suratgan.backend.order.domain.StoreInfo;
import com.suratgan.backend.order.domain.service.OrderCheck;
import com.suratgan.backend.order.infrastructure.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCheck orderCheck;

    @Transactional
    public OrderId createOrder(OrderServiceDto.Create request) {

        UUID memberId = UUID.randomUUID(); // TODO: 회원 ID는 인증된 사용자로부터 가져와야 합니다.(테스트용)

        Orderer orderer = Orderer.of(
            memberId,
            request.getOrdererName(),
            request.getOrdererMobile(),
            request.getOrdererEmail()
        );

        StoreInfo storeInfo = StoreInfo.of(
            request.getStoreId(),
            request.getStoreName(),
            request.getStoreAddress(),
            request.getStoreTel()
        );

        List<OrderItem> items = request.getItems().stream()
                .map(i -> {
                        ProductInfo productInfo = ProductInfo.of(
                            i.getMenuId(),
                            "임시메뉴",
                            10000
                        );

                        return OrderItem.builder()
                            .item(productInfo)
                            .quantity(i.getQuantity())
                            .build();
                })
                .toList();

        Order order = Order.create(orderer, storeInfo, items, orderCheck);

        order.orderAccept(); // TODO: 테스트용(반드시 삭제)

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다."));

        order.cancel(LocalDateTime.now());
    }
}
