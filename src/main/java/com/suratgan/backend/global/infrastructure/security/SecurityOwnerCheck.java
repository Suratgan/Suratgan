package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.UserDetails;
import com.suratgan.backend.store.domain.QStore;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.StoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityOwnerCheck implements OwnerCheck {

    private final StoreRepository storeRepository;
    private final UserDetails userDetails;

    @Override
    public boolean isOwner(UUID storeId) {

        if (storeId == null) return false;

        UUID ownerId = userDetails.getId();
        if (ownerId == null || !userDetails.isAuthenticated()) {
            return false;
        }

        Store store = storeRepository
            .findById(StoreId.of(storeId))
            .orElse(null);

        if (store == null) return false;

        return ownerId.equals(store.getOwner().getUserId());
    }

    @Override
    public UUID getOwnerId() {
        return userDetails.getId();
    }

    @Override
    public String getOwnerName() {
        return userDetails.getName();
    }

    @Override
    public UUID getStoreId() {

        Store store = storeRepository
            .findAll()
            .stream()
            .filter(s -> s.getOwner().getUserId().equals(userDetails.getId()))
            .findFirst()
            .orElse(null);

        return store == null ? null : store.getId().getId();
    }

    @Override
    public String getOwnerRole() {
        return "OWNER";
    }
}