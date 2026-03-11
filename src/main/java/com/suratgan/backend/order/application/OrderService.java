package com.suratgan.backend.order.application;

import com.suratgan.backend.global.domain.service.CustomerCheck;
import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.global.domain.service.UserDetails;
import com.suratgan.backend.global.infrastructure.event.Events;
import com.suratgan.backend.order.application.dto.OrderServiceDto;
import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.domain.OrderItem;
import com.suratgan.backend.global.infrastructure.event.Events;
import com.suratgan.backend.order.domain.event.OrderCreatedEvent;
import com.suratgan.backend.order.domain.ProductInfo;
import com.suratgan.backend.order.domain.StoreInfo;
import com.suratgan.backend.order.domain.event.OrderCreatedEvent;
import com.suratgan.backend.order.domain.service.OrderCheck;
import com.suratgan.backend.order.infrastructure.OrderRepository;
import com.suratgan.backend.order.presentation.dto.OrderCreateResponse;
import com.suratgan.backend.store.domain.Menu;
import com.suratgan.backend.store.domain.MenuId;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.StoreRepository;
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
    private final StoreRepository storeRepository;
    private final OrderCheck orderCheck;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final CustomerCheck customerCheck;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderCreateResponse createOrder(OrderServiceDto.Create request, UserDetails userDetails) {

        // 1. 가게 조회
        Store store = storeRepository
            .findById(StoreId.of(request.getStoreId()))
            .orElseThrow(() -> new NoSuchElementException("가게를 찾을 수 없습니다."));

        StoreInfo storeInfo = StoreInfo.of(
            store.getId().getId(),
            store.getStoreName(),
            store.getLocation().getAddress()
        );

        // 2. 주문 아이템 생성
        List<OrderItem> items = request.getItems().stream()
            .map(i -> {

                Menu menu = store.getMenu(MenuId.of(i.getMenuId()));

                if (menu == null) {
                    throw new NoSuchElementException("메뉴를 찾을 수 없습니다.");
                }

                ProductInfo productInfo = ProductInfo.of(
                    i.getMenuId(),
                    menu.getName(),
                    menu.getPrice()
                );

                return OrderItem.builder()
                    .item(productInfo)
                    .quantity(i.getQuantity())
                    .build();
            })
            .toList();

        // 3. 주문 생성
        Order order = Order.builder()
            .orderId(null)
            .ordererName(request.getOrdererName())
            .ordererMobile(request.getOrdererMobile())
            .ordererEmail(request.getOrdererEmail())
            .storeId(storeInfo.getStoreId())
            .storeName(storeInfo.getStoreName())
            .storeAddress(storeInfo.getStoreAddress())
            .orderItems(items)
            .customerCheck(customerCheck)
            .orderCheck(orderCheck)
            .userDetails(userDetails)
            .build();

        orderRepository.save(order);

        // 결제 요청 이벤트 발생
        Events.trigger(
            new OrderCreatedEvent(
                order.getId().getId(),
                order.getTotalOrderPrice().getValue()
            )
        );

        return new OrderCreateResponse(
                order.getId().getId(),
                order.getTotalOrderPrice().getValue(),
                order.getStoreInfo().getStoreName()
        );
    }

    /**
     * 주문 접수
     */
    @Transactional
    public void acceptOrder(UUID orderId) {

        Order order = findOrder(orderId);

        order.orderAccept(roleCheck, ownerCheck, orderCheck);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(UUID orderId) {

        Order order = findOrder(orderId);

        order.cancel(roleCheck, ownerCheck, orderCheck);
    }

    /**
     * 배송 시작
     */
    @Transactional
    public void startDelivery(UUID orderId) {

        Order order = findOrder(orderId);

        order.delivery(roleCheck, ownerCheck, orderCheck);
    }

    /**
     * 주문 완료 처리
     */
    @Transactional
    public void completeOrder(UUID orderId) {

        Order order = findOrder(orderId);

        order.done(roleCheck, ownerCheck);
    }

    /**
     * 공통 주문 조회
     */
    private Order findOrder(UUID orderId) {

        return orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다."));
    }

    @Transactional
    public void deliveryDone(UUID orderId) {

        Order order = findOrder(orderId);

        order.deliveryDone(roleCheck, ownerCheck, orderCheck);
    }

    @Transactional
    public void preparing(UUID orderId) {
        Order order = findOrder(orderId);
        order.preparing(roleCheck, ownerCheck, orderCheck);
    }
}