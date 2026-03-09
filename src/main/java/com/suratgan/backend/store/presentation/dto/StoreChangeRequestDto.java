package com.suratgan.backend.store.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreChangeRequestDto {
    @NotBlank
    String storeName;

    @NotBlank
    String owerName;

    @NotEmpty
    List<String> categories;

    @NotBlank
    String address;
}
