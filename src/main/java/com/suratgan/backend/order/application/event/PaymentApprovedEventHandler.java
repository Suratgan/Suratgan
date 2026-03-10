package com.suratgan.backend.order.application.event;

import com.suratgan.backend.order.domain.Order;
import com.suratgan.backend.order.domain.OrderId;
import com.suratgan.backend.order.domain.exception.OrderNotFoundException;
import com.suratgan.backend.order.infrastructure.OrderRepository;
import com.suratgan.backend.payment.domain.PaymentApprovedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 결제 승인 후 주문 후속 처리 이벤트 핸들러
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentApprovedEventHandler {

    private final OrderRepository orderRepository;

    @Async
    @Retryable(
        retryFor = { Exception.class },
        noRetryFor = { OrderNotFoundException.class }, // 주문서가 없다면 재시도는 무의미
        maxAttempts = 5,
        backoff = @Backoff(delay = 5000, multiplier = 2.0)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentApprovedEvent event) {
        Order order = getOrder(event.getOrderId());
        order.paymentConfirm();
    }

    @Recover // 모든 재시도가 실패했을 때 호출되는 보상 트랜잭션
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recover(Exception e, PaymentApprovedEvent event) {
        log.error("주문 상태 변경 최종 실패. 사유: {}. 자동 환불을 진행합니다. 주문ID: {}",
            e.getMessage(), event.getOrderId(), e);
        Order order = getOrder(event.getOrderId());
        order.failPaymentConfirm();

    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(OrderId.of(orderId)).orElseThrow(OrderNotFoundException::new);
    }
}
