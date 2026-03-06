package com.suratgan.backend.category.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoryDto {
    private UUID id;

    @NotBlank
    private String category;
}
