package com.suratgan.backend.order.presentation;

import com.suratgan.backend.order.application.OrderService;
import com.suratgan.backend.order.application.dto.OrderServiceDto;
import com.suratgan.backend.order.domain.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderId createOrder(@RequestBody OrderServiceDto.Create request) {
        return orderService.createOrder(request);
    }
}
