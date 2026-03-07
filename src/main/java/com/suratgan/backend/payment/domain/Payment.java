package com.suratgan.backend.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "paymentkey")
    private String paymentKey; // 토스에서 보내주는 key값

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PaymentStatus status; // 결제 상태

    private int amount; // 가격

    @Column(name = "log", columnDefinition = "jsonb")
    private String paymentLog; //결제 내역

    @Column(length=30)
    private String method; // 결제 수단

    @Column(updatable = false)
    private LocalDateTime createdAt; //생성 시간

    @Column(insertable = false)
    private LocalDateTime approvedAt; //승인 시간

    private String cancelReason; //취소 사유

    @Column(insertable = false)
    private LocalDateTime canceledAt; //취소 시간


    public static Payment create(UUID orderId, int amount) {
        if (orderId == null) throw new IllegalArgumentException("orderId is null");
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");

        Payment p = new Payment();
        p.id = UUID.randomUUID();
        p.orderId = orderId;
        p.amount = amount;
        p.status = PaymentStatus.READY;
        p.createdAt = LocalDateTime.now();
        return p;
    }

    public void approve(String paymentKey, int approvedAmount, String paymentLog, String method) {
        if (status != PaymentStatus.READY) throw new IllegalStateException("not READY");
        if (paymentKey == null || paymentKey.isBlank()) throw new IllegalArgumentException("paymentKey required");

        if (this.amount != approvedAmount) {
            throw new IllegalArgumentException("결제 금액 불일치");
        }

        this.paymentKey = paymentKey;
        this.paymentLog = paymentLog;
        this.method = method;
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public void cancel(String cancelReason, String cancelLog, LocalDateTime canceledAt) {
        if (status == PaymentStatus.CANCELED) return;
        if (status != PaymentStatus.APPROVED) throw new IllegalStateException("not APPROVED");
        if (paymentKey == null || paymentKey.isBlank()) throw new IllegalStateException("paymentKey missing");

        if (cancelLog != null && !cancelLog.isBlank()) {
            this.paymentLog = cancelLog;
        } else if (cancelReason != null && !cancelReason.isBlank()) {
            this.paymentLog = cancelReason;
        }

        this.status = PaymentStatus.CANCELED;
        this.cancelReason = cancelReason;
        this.canceledAt = canceledAt != null ? canceledAt : LocalDateTime.now();
    }

    public void fail(String message) {
        this.status = PaymentStatus.FAILED;
        this.paymentLog = message;
    }
}