package com.suratgan.backend.order.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderServiceDto {

    @Getter
    @NoArgsConstructor
    public static class Create {
        private String ordererName;
        private String ordererMobile;
        private String ordererEmail;

        private UUID storeId;
        private String storeName;
        private String storeAddress;
//        private String storeTel;

        private List<Item> items;
    }

    @Getter
    @NoArgsConstructor
    public static class Item {

        private int menuId;
        private int quantity;
    }
}
