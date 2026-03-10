package com.suratgan.backend.global.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {
    private int value;

    public Price(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
        this.value = value;
    }

    public Price add(Price price) {
        return new Price(this.value + price.value);
    }

    public Price multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("곱셈의 배수는 음수가 될 수 없습니다.");
        }
        return new Price(this.value * multiplier);
    }
    public int getValue() {
        return value;
    }
}
