package com.suratgan.backend.category.presentation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponseDto {
    private UUID id;
    private String category;
}
