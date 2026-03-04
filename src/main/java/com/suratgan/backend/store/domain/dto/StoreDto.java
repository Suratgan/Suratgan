package com.suratgan.backend.store.domain.dto;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.service.CategoryCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class StoreDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class CategoryDto {
        private RoleCheck roleCheck;
        private OwnerCheck ownerCheck;
        private CategoryCheck categoryCheck;
        private List<UUID> categoryIds;
    }
}
