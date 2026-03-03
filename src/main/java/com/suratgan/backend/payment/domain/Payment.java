package com.suratgan.backend.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String paymentKey; // 토스에서 보내주는 key값

    private PaymentStatus status; // 결제 상태

    private long amount; // 가격

    private String paymentLog; //결제 내역

    private LocalDateTime createdAt; //생성 시간

    private LocalDateTime approvedAt; //승인 시간

    private LocalDateTime canceledAt; //취소 시간


}
