package com.suratgan.backend.order.infrastructure;

import com.suratgan.backend.global.domain.service.UserDetails;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.domain.OrderItem;
import com.suratgan.backend.order.domain.service.OrderCheck;
import com.suratgan.backend.store.domain.StoreRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCheckImpl implements OrderCheck {

    @Override
    public boolean isOrderable(UUID storeId, List<OrderItem> items) {
        // TODO: 실제 주문 가능 여부 판단 로직 구현(우선은 서버가 뜰 수 있도록 기본값 반환)
        return true;
    }

    @Override
    public boolean isMyOrder(OrderId orderId) {
        return true; // TODO 인증 붙으면 실제 구현
    }
}
