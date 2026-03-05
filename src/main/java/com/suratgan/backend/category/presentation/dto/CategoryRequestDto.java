package com.suratgan.backend.category.presentation.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public class CategoryRequestDto {
    private UUID id;

    @NotBlank
    private List<String> categories;
}
