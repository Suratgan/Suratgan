package com.suratgan.backend.order.domain;

import com.suratgan.backend.global.domain.Price;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Embedded
    private ProductInfo item;

    private int quantity;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Price totalPrice; // (상품가) * (수량)

    @Builder
    protected OrderItem(ProductInfo item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        this.item = item;
        this.quantity = quantity;

        // 총 가격 계산
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = item.getPrice().multiply(quantity);
    }

}
