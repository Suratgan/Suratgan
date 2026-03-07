package com.suratgan.backend.order.domain;

import jakarta.persistence.Column;
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
public class StoreInfo {
    @Column(nullable = false)
    private UUID storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private String storeTel;

    public static StoreInfo of(UUID storeId, String storeName, String storeAddress, String storeTel) {
        return new StoreInfo(storeId, storeName, storeAddress, storeTel);
    }
}
