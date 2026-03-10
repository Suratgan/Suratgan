package com.suratgan.backend.store.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreSearchDto {
    private String storeName;
    private Double latitude;
    private Double longitude;
    private List<String> categoryNames;
}
