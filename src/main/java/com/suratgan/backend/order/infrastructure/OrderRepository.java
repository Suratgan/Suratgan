package com.suratgan.backend.order.infrastructure;

import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, OrderId> {

}
