package com.suratgan.backend.payment.application.service;

import com.suratgan.backend.payment.application.dto.ApprovePaymentResult;
import com.suratgan.backend.payment.domain.Payment;
import com.suratgan.backend.payment.domain.PaymentApprovedEvent;
import com.suratgan.backend.payment.domain.PaymentRepository;
import com.suratgan.backend.payment.infrastructure.api.TossApprovePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovePaymentService implements ApprovePayment {

    private final PaymentRepository paymentRepository;
    private final TossApprovePayment tossApprovePayment;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ApprovePaymentResult approve(String orderIdForToss, String paymentKey, long amount) {
        if (orderIdForToss == null || orderIdForToss.isBlank()) {
            throw new IllegalArgumentException("orderIdForToss required");
        }
        if (paymentKey == null || paymentKey.isBlank()) {
            throw new IllegalArgumentException("paymentKey required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        UUID orderId = UUID.fromString(orderIdForToss);
        int approveAmount = (int) amount;

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseGet(() -> saveOrGetExisting(orderId, approveAmount));

        try {
            TossApprovePayment.TossApproveResponse tossRes =
                    tossApprovePayment.approve(paymentKey, orderIdForToss, amount);

            payment.approve(
                    tossRes.getPaymentKey(),
                    (int) tossRes.getTotalAmount(),
                    tossRes.toString(),
                    null
            );

            eventPublisher.publishEvent(new PaymentApprovedEvent(orderId));

        } catch (Exception e) {
            payment.fail(e.getMessage());
            throw e;
        }

        return ApprovePaymentResult.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentKey(payment.getPaymentKey())
                .amount(payment.getAmount())
                .status(payment.getStatus().name())
                .approvedAt(payment.getApprovedAt())
                .build();
    }

    private Payment saveOrGetExisting(UUID orderId, int amount) {
        try {
            return paymentRepository.save(Payment.create(orderId, amount));
        } catch (DataIntegrityViolationException e) {
            return paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> e);
        }
    }
}