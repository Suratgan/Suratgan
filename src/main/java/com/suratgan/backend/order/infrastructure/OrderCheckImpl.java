package com.suratgan.backend.order.infrastructure;

import com.suratgan.backend.order.domain.OrderItem;
import com.suratgan.backend.order.domain.service.OrderCheck;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderCheckImpl implements OrderCheck {

    @Override
    public boolean isOrderable(UUID storeId, List<OrderItem> items) {
        // TODO: 실제 주문 가능 여부 판단 로직 구현(우선은 서버가 뜰 수 있도록 기본값 반환)
        return true;
    }
}
