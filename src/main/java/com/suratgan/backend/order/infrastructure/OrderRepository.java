package com.suratgan.backend.order.infrastructure;

import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, OrderId> {

    // 고객 주문 목록 조회
    List<Order> findByOrderer_Id(UUID Userid);

    // 매장 주문 목록 조회
    List<Order> findByStoreInfoStoreId(UUID storeId);
}
