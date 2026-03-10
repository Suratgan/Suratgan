package com.suratgan.backend.store.application.dto;

import com.suratgan.backend.store.domain.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponseDto {
    private int menuIdx;
    private String menuName;
    private String menuInfo;
    private Integer price;
    private String menuImg;

    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
                .menuIdx(menu.getMenuId().getMenuIdx())
                .menuName(menu.getName())
                .menuInfo(menu.getMenuInfo())
                .price(menu.getPrice())
                .menuImg(menu.getMenuImg())
                .build();
    }
}
