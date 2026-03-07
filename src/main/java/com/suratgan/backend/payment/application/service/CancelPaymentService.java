package com.suratgan.backend.payment.application.service;

import com.suratgan.backend.payment.application.dto.CancelPaymentResult;
import com.suratgan.backend.payment.domain.Payment;
import com.suratgan.backend.payment.domain.PaymentRepository;
import com.suratgan.backend.payment.infrastructure.api.TossCancelPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CancelPaymentService implements CancelPayment {

    private final PaymentRepository paymentRepository;
    private final TossCancelPayment tossCancelPayment;

    @Override
    public CancelPaymentResult cancel(UUID paymentId, String cancelReason) {
        if (paymentId == null) throw new IllegalArgumentException("paymentId required");
        if (cancelReason == null || cancelReason.isBlank()) throw new IllegalArgumentException("cancelReason required");

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found: " + paymentId));

        TossCancelPayment.TossCancelResponse tossRes =
                tossCancelPayment.cancel(payment.getPaymentKey(), cancelReason);

        LocalDateTime canceledAt = parseCanceledAtOrNow(tossRes.getCanceledAt());

        payment.cancel(cancelReason, null, canceledAt);

        return CancelPaymentResult.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .status(payment.getStatus().name())
                .cancelReason(payment.getCancelReason())
                .canceledAt(payment.getCanceledAt())
                .build();
    }

    private LocalDateTime parseCanceledAtOrNow(String canceledAt) {
        if (canceledAt == null || canceledAt.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            return OffsetDateTime.parse(canceledAt).toLocalDateTime();
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}