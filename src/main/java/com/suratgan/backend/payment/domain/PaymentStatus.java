package com.suratgan.backend.payment.domain;

public enum PaymentStatus {
    READY, //결제 준비
    APPROVED, //결제 승인
    CANCELED, // 결제 취소
    FAILED, // 결제 실패
    EXPIRED // 결제 시간 만료
}
