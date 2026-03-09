package com.suratgan.backend.global.domain.service;

import java.util.UUID;

public interface OwnerCheck {
    boolean isOwner(UUID storeId);
    UUID getOwnerId();
    String getOwnerName();
    UUID getStoreId();
    String getOwnerRole();
}
