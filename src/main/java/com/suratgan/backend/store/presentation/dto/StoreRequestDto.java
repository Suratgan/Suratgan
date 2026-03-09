package com.suratgan.backend.store.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreRequestDto {
    @NotBlank
    String name;

    @NotBlank
    List<String> categories;

    @NotBlank
    String address;
}
