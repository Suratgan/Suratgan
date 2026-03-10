package com.suratgan.backend.payment.application.service;

import com.suratgan.backend.payment.domain.Payment;
import com.suratgan.backend.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatePaymentService implements CreatePayment {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(UUID orderId, int amount) {
        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            return;
        }

        Payment payment = Payment.create(orderId, amount);

        paymentRepository.save(payment);
    }
}