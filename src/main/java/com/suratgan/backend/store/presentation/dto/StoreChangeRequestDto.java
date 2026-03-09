package com.suratgan.backend.store.presentation.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreChangeRequestDto {
    String storeName;

    String owerName;

    List<String> categories;

    String address;
}
