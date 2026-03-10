package com.suratgan.backend.payment.application.event;

import com.suratgan.backend.order.domain.event.OrderCreatedEvent; // [수정] 변경된 이벤트 클래스
import com.suratgan.backend.payment.application.service.CreatePayment;
import com.suratgan.backend.payment.domain.event.PaymentCreateFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAcceptedEventHandler {

    private final CreatePayment createPayment;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Retryable(
            retryFor = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000, multiplier = 2.0)
    )
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신. 결제 데이터 생성을 시작합니다. 주문ID: {}, 금액: {}",
                event.orderId(), event.amount());

        createPayment.create(event.orderId(), event.amount());
    }

    @Recover
    public void recover(Exception e, OrderCreatedEvent event) {
        log.error("결제 데이터 생성 최종 실패. 주문ID: {}", event.orderId());
        eventPublisher.publishEvent(new PaymentCreateFailedEvent(event.orderId()));
    }
}