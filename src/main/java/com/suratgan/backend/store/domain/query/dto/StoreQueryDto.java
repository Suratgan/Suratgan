package com.suratgan.backend.store.domain.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class StoreQueryDto {
    @Getter
    @Builder
    public static class Search {
        private String storeName;
        private Double latitude;
        private Double longitude;
        private List<UUID> categoryIds;
    }
}
