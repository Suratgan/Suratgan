package com.suratgan.backend.order.domain;

public enum OrderStatus {
    ORDER_CREATING,     // 주문 생성 중
    ORDER_ACCEPT,       // 주문 접수
    PAYMENT_CONFIRM,    // 입금 확인
    PREPARING,          // 배달 준비 중
    DELIVERY,           // 배달 중
    DELIVERY_DONE,      // 배달 완료
    ORDER_DONE,         // 주문 처리 완료
    ORDER_CANCEL,       // 주문 취소(미입금)
    ORDER_REFUND,       // 주문 환불(입금 후 취소)
    EXCHANGE            // 교환
}
