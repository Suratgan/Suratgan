package com.suratgan.backend.store.application.dto;

import com.suratgan.backend.store.domain.Menu;
import com.suratgan.backend.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class StoreResponseDto {
    private UUID id;
    private String name;
    private double rating;
    private long reviewCnt;
    private List<String> menuImages;

    public static StoreResponseDto from(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId().getId())
                .name(store.getStoreName())
                .rating(store.getRating())
                .reviewCnt(store.getReviewCnt())
                .menuImages(
                        store.getMenus().stream()
                                .filter(Menu::isVisible)
                                .map(Menu::getMenuImg)
                                .filter(image -> image != null && !image.isBlank())
                                .toList()
                )
                .build();
    }
}
