package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.UserDetails;
import com.suratgan.backend.store.domain.QStore;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OwerCheckImpl implements OwnerCheck {
    private final StoreRepository storeRepository;
    private final UserDetails userDetails;

    @Override
    public boolean isOwner(UUID storeId) {
        if (storeId == null) return false;

        UUID ownerId = userDetails.getId();
        if (ownerId == null || !userDetails.isAuthenticated()) {
            return false;
        }

        QStore store = QStore.store;
        return storeRepository.exists(
                store.id.id.eq(storeId)
                        .and(store.owner.userId.eq(ownerId))
        );
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
        QStore store = QStore.store;
        Store item = storeRepository.findOne(store.owner.userId.eq(userDetails.getId())).orElse(null);
        return item == null ? null : item.getId().getId();
    }

    @Override
    public String getOwnerRole() {
        return userDetails.getRole();
    }
}
