package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.service.AddressToCoords;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreLocation {
    @Column(length = 2000)
    private String address;

    private double longitude;
    private double latitude;

    protected StoreLocation(String address, AddressToCoords addressToCoords) {
        this.address = address;
        if (!StringUtils.hasText(address)) return;

        // 주소를 위도, 경도로 변환
        double[] coords = addressToCoords.convert(address);
        latitude = coords[0]; longitude = coords[1];
    }
}
