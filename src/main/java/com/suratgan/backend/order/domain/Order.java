package com.suratgan.backend.order.domain;

import static com.suratgan.backend.order.domain.OrderStatus.ORDER_ACCEPT;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.domain.Price;
import com.suratgan.backend.order.domain.service.OrderCheck;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 0. 주문은 반드시 회원의 권한을 가진 사용자만 가능
 * 1. 주문 상품이 1개 이상이어야 주문이 가능
 * 2. 주문 상품은 주문이 가능한 상품인지 체크 한다.
 *    - 1. 매장의 영업 여부 체크: Store::isVisible()
 *    - 2. 매장의 주문 가능 시간 체크: Store::isOrderable()
 *    - 3. 상품의 주문 가능 여부 체크: Product::isOrderable()
 *    - 주문 상품에는 여러 매장이 있을 수 있으므로 목록에서 체크
 *
 * 3. 주문 상품의 총 금액은 주문 상품의 총 금액은 주문 상품 목록을 통해서만 계산된다.
 * 4. 주문 취소는 주문 접수 후 5분 이내 가능
 *    - 입금 확인 전: 주문 취소(ORDER_CANCEL)
 *    - 입금 확인 후: 주문 환불 상태(ORDER_REFUND) / 결제 취소 진행(이벤트 발생)
 * 5. 배송 중 주문 상태는 입금 확인이 되어야만 변경 가능
 * 6. 배송 정보 변경은 배송 중 이전 단계에서만 가능
 * 7. 주문 완료(ORDER_DONE)으로 변경하면 후기 작성 요청 이벤트를 발생시킨다.
 */
@Entity
@Getter
@ToString
@Table(name = "P_ORDER")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @EmbeddedId
    private OrderId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private Orderer orderer;

    @Embedded
    private StoreInfo storeInfo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_ORDER_ITEM", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItem> orderItems = new ArrayList<>(); // null 방지

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total_amount"))
    private Price totalOrderPrice; // 주문 상품 목록을 통해 계산된 총 주문 금액

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 주문 상품 목록 설정
    private void setOrderItems(List<OrderItem> orderItems, OrderCheck orderCheck) {

        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 상품은 1개 이상이어야 합니다.");
        }

        if (!orderCheck.isOrderable(storeInfo.getStoreId(), orderItems)) {
            throw new IllegalArgumentException("주문 상품 중 주문이 불가능한 상품이 있습니다.");
        }

        this.orderItems = new ArrayList<>(orderItems);  // 외부 리스트 변경 방지 위해 복사본 생성
        calculateTotalOrderPrice(); // 주문 상품 목록을 설정할 때 총 주문 금액 계산
    }

    // 주문 생성
    public static Order create(Orderer orderer, StoreInfo storeInfo, List<OrderItem> items, OrderCheck orderCheck) {
        if (orderer == null) {
            throw new IllegalArgumentException("주문자는 필수입니다.");
        }
        if (storeInfo == null) {
            throw new IllegalArgumentException("매장 정보는 필수입니다.");
        }
        if (orderCheck == null) {
            throw new IllegalArgumentException("OrderCheck는 필수입니다.");
        }

        Order order = new Order();
        order.id = OrderId.of();
        order.orderer = orderer;
        order.storeInfo = storeInfo;
        order.status = OrderStatus.ORDER_CREATING;

        order.setOrderItems(items, orderCheck);

        return order;
    }

    // 금액 연산(add)은 Price VO가 담당하고 Order는 합산만 수행
    private void calculateTotalOrderPrice() {
        Price total = new Price(0);

        for (OrderItem item : orderItems) {
            total = total.add(item.getTotalPrice());
        }

        this.totalOrderPrice = total;
    }

    // 주문 접수
    public void orderAccept() {
        if (status != OrderStatus.ORDER_CREATING) {
            throw new IllegalStateException("주문 생성 상태에서만 접수 가능합니다.");
        }
        this.status = ORDER_ACCEPT;
    }

    // 주문 취소
    public void cancel(LocalDateTime now) {
        if (status != ORDER_ACCEPT) {
            throw new IllegalStateException("주문 접수 상태에서만 취소 가능합니다.");
        }

        if (createdAt == null) {
            throw new IllegalStateException("주문 생성 시간이 존재하지 않습니다.");
        }

        if (createdAt.plusMinutes(5).isBefore(now)) {
            throw new IllegalStateException("주문 접수 후 5분이 지나 취소가 불가능합니다.");
        }
        this.status = OrderStatus.ORDER_CANCEL;
    }
}
