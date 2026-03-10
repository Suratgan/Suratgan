package com.suratgan.backend.address.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateAddressRequestDto {

    @NotBlank(message = "address는 필수입니다.")
    private String address;

    private String detailAddress;
}
