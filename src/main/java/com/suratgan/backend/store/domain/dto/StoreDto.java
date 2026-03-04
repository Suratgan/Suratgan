package com.suratgan.backend.store.domain.dto;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.Menu;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.service.CategoryCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class StoreDto {
    @Getter
    @Builder
    public static class MenuDto {
        private RoleCheck roleCheck;
        private OwnerCheck ownerCheck;
        private String name;
        private String menuInfo;
        private int price;
        private String menuImg;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CategoryDto {
        private RoleCheck roleCheck;
        private OwnerCheck ownerCheck;
        private CategoryCheck categoryCheck;
        private List<UUID> categoryIds;
    }

    public static Menu toMenu(StoreId id, int menuIdx, MenuDto dto) {
        return Menu.builder()
                .storeId(id)
                .menuIdx(menuIdx)
                .name(dto.getName())
                .menuInfo(dto.getMenuInfo())
                .price(dto.getPrice())
                .menuImg(dto.getMenuImg())
                .build();
    }
}
