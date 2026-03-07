package com.suratgan.backend.order.domain;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderId {

    private UUID id;

    public static OrderId of() {
        return OrderId.of(UUID.randomUUID());
    }

    public static OrderId of(UUID id) {
        return new OrderId(id);
    }
}
