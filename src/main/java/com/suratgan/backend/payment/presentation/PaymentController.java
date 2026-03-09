package com.suratgan.backend.payment.presentation;

import com.suratgan.backend.payment.application.dto.ApprovePaymentResult;
import com.suratgan.backend.payment.application.dto.CancelPaymentResult;
import com.suratgan.backend.payment.application.service.ApprovePayment;
import com.suratgan.backend.payment.application.service.CancelPayment;
import com.suratgan.backend.payment.presentation.dto.ApprovePaymentRequest;
import com.suratgan.backend.payment.presentation.dto.CancelPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final ApprovePayment approvePayment;
    private final CancelPayment cancelPayment;

    @PostMapping("/approve")
    public ResponseEntity<ApprovePaymentResult> approve(@RequestBody ApprovePaymentRequest request) {
        return ResponseEntity.ok(
                approvePayment.approve(
                        request.getOrderId(),
                        request.getPaymentKey(),
                        request.getAmount()
                )
        );
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<CancelPaymentResult> cancel(@PathVariable UUID paymentId,
                                                      @RequestBody CancelPaymentRequest request) {
        return ResponseEntity.ok(
                cancelPayment.cancel(paymentId, request.getCancelReason())
        );
    }
}