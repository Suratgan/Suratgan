package com.suratgan.backend.order.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    private String productName;
    private int quantity;
    private int price;

    protected OrderItem(String productName, int quantity, int price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // DDD에서는 생성자를 Protected로 막아두고 정적 팩토리 메서드를 통해 객체를 생성하는 것을 권장한다.
    public static OrderItem of(String productName, int quantity, int price) {
        return new OrderItem(productName, quantity, price);
    }

    public int getTotalPrice() {
        return quantity * price;
    }
}
