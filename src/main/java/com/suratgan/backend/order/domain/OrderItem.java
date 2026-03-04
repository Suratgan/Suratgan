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

    public int getTotalPrice() {
        return quantity * price;
    }
}
