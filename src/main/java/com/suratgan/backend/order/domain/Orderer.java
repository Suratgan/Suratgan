package com.suratgan.backend.order.domain;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orderer {

    private UUID id;
    private String name;
    private String mobile;
    private String email;

    protected Orderer(UUID id, String name, String mobile, String email) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    // DDD에서는 생성자를 Protected로 막아두고 정적 팩토리 메서드를 통해 객체를 생성하는 것을 권장한다.
    public static Orderer of(UUID id, String name, String mobile, String email) {
        return new Orderer(id, name, mobile, email);
    }
}
