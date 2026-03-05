package com.suratgan.backend.order.domain;

import com.suratgan.backend.global.domain.Price;
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
public class ProductInfo {
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Builder
    protected ProductInfo(String code, String name, int price) {
        this.code = code;
        this.name = name;
        this.price = new Price(price);
    }
}
