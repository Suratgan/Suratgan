package com.suratgan.backend.order.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.domain.Price;
import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.global.domain.service.UserDetails;
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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

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

    // 주문 생성
    @Builder
    public Order(UUID orderId, String ordererName, String ordererMobile, String ordererEmail, UUID storeId, String storeName, String storeAddress, String storeTel, List<OrderItem> orderItems, String deliveryAddress, String deliveryAddressDetail, String deliveryMemo, OrderCheck orderCheck, UserDetails userDetails) {

        // 로그인 체크 여부
        checkAuthenticated(userDetails);

        this.id = orderId == null ? OrderId.of() : OrderId.of(orderId);
        this.orderer = new Orderer(
            userDetails.getId(),
            StringUtils.hasText(ordererName) ? ordererName : userDetails.getName(),
            StringUtils.hasText(ordererMobile) ? ordererMobile : userDetails.getMobile(),
            StringUtils.hasText(ordererEmail) ? ordererEmail : userDetails.getEmail()
        );
        this.storeInfo = new StoreInfo(storeId, storeName, storeAddress);
        this.status = OrderStatus.ORDER_CREATING; // 주문 생성 중
        setOrderItems(orderItems, orderCheck);
    }

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

    // 금액 연산(add)은 Price VO가 담당하고 Order는 합산만 수행
    private void calculateTotalOrderPrice() {
        Price total = new Price(0);

        for (OrderItem item : orderItems) {
            total = total.add(item.getTotalPrice());
        }

        this.totalOrderPrice = total;
    }

    // 주문 접수
    public void orderAccept(RoleCheck roleCheck, OwnerCheck ownerCheck, OrderCheck orderCheck) {
        // 권한 체크
        checkAuthority(roleCheck, ownerCheck, orderCheck);

        changeStatus(OrderStatus.ORDER_ACCEPT);
    }

    // 결제 완료 - SYSTEM에서 자동 처리되므로 권한 체크는 생략
    public void paymentConfirm() {
        changeStatus(OrderStatus.PAYMENT_CONFIRM);
    }

    // 환불 상태 변환 - SYSTEM에서 자동 처리되므로 권한 체크는 생략
    public void failPaymentConfirm() {
        changeStatus(OrderStatus.ORDER_REFUND);
    }

    // 주문 취소
    public void cancel(RoleCheck roleCheck, OwnerCheck ownerCheck, OrderCheck orderCheck) {
        // 권한 체크
        checkAuthority(roleCheck, ownerCheck, orderCheck);

        // 주문 접수 상태(입금 전) + 5분 이내 취소 시 -> 단순 주문 취소
        if (status == OrderStatus.ORDER_ACCEPT) {
            if (createdAt == null) {
                throw new IllegalStateException("주문 생성 시간이 존재하지 않습니다.");
            }

            if (LocalDateTime.now().isBefore(createdAt.plusMinutes(5L))) {
                changeStatus(OrderStatus.ORDER_CANCEL);
            } else {
                throw new IllegalStateException("주문 접수 후 5분이 지나면 주문 취소가 불가능합니다.");
            }
        }
        // 입금 확인 상태(PAYMENT_CONFIRM)에서 취소 시 -> 환불 처리 및 이벤트 발생
        else if (status == OrderStatus.PAYMENT_CONFIRM) {
            changeStatus(OrderStatus.ORDER_REFUND);
        } else {
            throw new IllegalStateException("현재 상태에서는 주문 취소가 불가능합니다. (현재 상태: " + status + ")");
        }
    }

        // 시스템 자동 취소
    public void systemCancel() {
        if (status != OrderStatus.ORDER_ACCEPT) {
            return;
        }
        changeStatus(OrderStatus.ORDER_CANCEL);
    }

    /**
     * 배송 시작
     * 배송 시작은 입금 확인 후 진행, 그러나 배송지가 매장에서 배송 가능한 지역이 아니라면 배송 불가 함
     * 매장별 배송 불가 지역 체크 필요, 그러나 이 기능은 다른 도메인 기능이 필요하므로 도메인 서비스로 추가, 단순히 도메인 서비스에 주문 도메인의 delivery상태 변경 로직은 실행
     */
    public void delivery(RoleCheck roleCheck, OwnerCheck ownerCheck, OrderCheck orderCheck) {
        if (status != OrderStatus.PAYMENT_CONFIRM) {
            throw new IllegalStateException(
                "입금 확인 상태에서만 배송 시작이 가능합니다. (현재 상태: " + status + ")"
            );
        }

        // 권한 체크
        checkAuthority(roleCheck, ownerCheck, orderCheck);

        changeStatus(OrderStatus.DELIVERY);
    }

    /**
     * 주문완료 처리
     * 후기 작성을 위해서 주문 완료 이벤트를 발생 시킵니다.
     * 주문완료 처리는 매장 점주, 관리자(MANAGER, MASTER)만 가능
     * 배송 완료(DELIVERY_DONE) 상태에서만 ORDER_DONE 상태로 변경 가능
     */
    public void done(RoleCheck roleCheck, OwnerCheck ownerCheck) {
        if (status != OrderStatus.DELIVERY_DONE) {
            throw new IllegalStateException("배송 완료된 주문만 최종 완료 처리가 가능합니다. (현재 상태: " + status + ")");
        }

        checkManagerOrOwnerAuthority(roleCheck, ownerCheck);

        changeStatus(OrderStatus.ORDER_DONE);

    }

    // 관리자 또는 점주 권한만 체크 (주문 완료용)
    private void checkManagerOrOwnerAuthority(RoleCheck roleCheck, OwnerCheck ownerCheck) {
        // 관리자 권한 확인
        if (roleCheck.hasRole(List.of("MASTER", "MANAGER"))) {
            return;
        }

        // 점주 권한 확인
        if (ownerCheck.isOwner(storeInfo.getStoreId())) {
            return;
        }

        throw new IllegalStateException("주문 완료 처리는 점주 또는 관리자만 가능합니다.");
    }

    /**
     * 주문서 정보 변경 가능 여부 체크
     *
     * 1. 자신의 주문은 수정 가능
     * 2. 주문서에 등록된 매장 ID의 점주인 경우 가능
     * 3. 관리자(MASTER, MANAGER)인 경우 가능
     */
    private void checkAuthority(RoleCheck roleCheck, OwnerCheck ownerCheck, OrderCheck orderCheck) {

        // 관리자 권한 확인
        if (roleCheck.hasRole(List.of("MASTER", "MANAGER"))) {
            return;
        }

        // 매장 점주 확인
        if (ownerCheck.isOwner(storeInfo.getStoreId())) {
            return;
        }

        // 본인 주문 확인
        if (orderCheck != null && orderCheck.isMyOrder(id)) {
            return;
        }

        throw new IllegalStateException("주문 정보 변경은 관리자, 매장 점주, 본인 주문인 경우에만 가능합니다.");
    }

    // 로그인 여부 체크
    private void checkAuthenticated(
        com.suratgan.backend.global.domain.service.UserDetails userDetails) {
        if (userDetails.getId() == null || !userDetails.isAuthenticated()) {
            throw new IllegalStateException("주문은 로그인한 사용자만 가능합니다.");
        }
    }

    // 주문 상태 변경 메서드(예: 입금 확인, 배달 준비, 배달 중 등)
    private void changeStatus(OrderStatus target) {

        if (this.status.isFinalStatus()) {
            throw new IllegalStateException("이미 종료된 주문 상태입니다. (현재 상태: " + status + ")");
        }

        if (!this.status.canChangeTo(target)) {
            throw new IllegalStateException(
                "현재 상태(" + status + ")에서 " + target + "로 변경할 수 없습니다."
            );
        }

        this.status = target;
    }
}
