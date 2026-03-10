package com.suratgan.backend.address.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddressResponseDto {
    private UUID id;
    private String address;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
}
