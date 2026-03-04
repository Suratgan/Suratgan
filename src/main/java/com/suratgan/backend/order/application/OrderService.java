package com.suratgan.backend.order.application;

import com.suratgan.backend.order.application.dto.OrderServiceDto;
import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.domain.OrderItem;
import com.suratgan.backend.order.domain.Orderer;
import com.suratgan.backend.order.domain.service.OrderCheck;
import com.suratgan.backend.order.infrastructure.OrderRepository;
import java.util.List;
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

        List<OrderItem> items = request.getItems().stream()
                .map(i -> OrderItem.of(
                    i.getProductName(),
                    i.getQuantity(),
                    i.getPrice()
                )).toList();

        Order order = Order.create(orderer, items, orderCheck);

        orderRepository.save(order);

        return order.getId();
    }
}
