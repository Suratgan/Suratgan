package com.suratgan.backend.order.domain;

public enum OrderStatus {
    ORDER_CREATING,     // 주문 생성 중
    PAYMENT_CONFIRM,    // 입금 확인
    ORDER_ACCEPT,       // 주문 접수
    PREPARING,          // 배달 준비 중
    DELIVERY,           // 배달 중
    DELIVERY_DONE,      // 배달 완료
    ORDER_DONE,         // 주문 처리 완료
    ORDER_CANCEL,       // 주문 취소(미입금)
    ORDER_REFUND,       // 주문 환불(입금 후 취소)
    EXCHANGE;           // 교환

    // 현재 상태에서 target 상태로 변경이 가능한지 여부를 반환하는 메서드
    public boolean canChangeTo(OrderStatus target) {

        if (target == null) {
            return false; // null로의 변경은 허용하지 않음
        }

        if (this == target) {
            return false; // 동일한 상태로의 변경은 허용하지 않음
        }

        return switch (this) {

            case ORDER_CREATING -> target == ORDER_ACCEPT || target == ORDER_CANCEL;
            case ORDER_ACCEPT -> target == PREPARING || target == ORDER_CANCEL;
            case PREPARING -> target == DELIVERY;
            case DELIVERY -> target == DELIVERY_DONE;
            case DELIVERY_DONE -> target == ORDER_DONE;

            default -> false;
        };
    }
    // 주문이 최종 상태인지 여부를 반환하는 메서드
    public boolean isFinalStatus() {
        return switch (this) {
            case ORDER_DONE, ORDER_CANCEL, ORDER_REFUND, EXCHANGE -> true;
            default -> false;
        };
    }
}
