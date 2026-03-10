package com.suratgan.backend.store.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCreateRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String menuInfo;

    @Min(0)
    private int price;

    private String menuImg;

    private boolean aiGenerated;
}
