package com.suratgan.backend.address.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Address extends BaseEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private String address;

    @Column
    private String detailAddress;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    public static Address create(UUID userId, String address, String detailAddress, Double latitude, Double longitude) {

        validate(address, longitude, latitude);

        return Address.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .address(address)
                .detailAddress(detailAddress)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    public void update(String address, String detailAddress, Double latitude, Double longitude) {
        validate(address, longitude, latitude);

        this.address = address;
        this.detailAddress = detailAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private static void validate(String address, Double longitude, Double latitude) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("주소는 비어 있을 수 없습니다.");
        }
        if (longitude == null || latitude == null) {
            throw new IllegalArgumentException("위도/경도는 필수입니다.");
        }
        if (longitude > 180 || longitude < -180) {
            throw new IllegalArgumentException("경도 범위가 올바르지 않습니다.");
        }
        if (latitude > 90 || latitude < -90) {
            throw new IllegalArgumentException("위도 범위가 올바르지 않습니다.");
        }
    }
}
