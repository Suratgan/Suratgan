package com.suratgan.backend.order.domain;

import com.suratgan.backend.global.domain.Price;
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

    @Builder
    protected OrderItem(ProductInfo item, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        this.item = item;
        this.quantity = quantity;
    }

    public Price getTotalPrice() {
        return item.getPrice().multiply(quantity);
    }
}
