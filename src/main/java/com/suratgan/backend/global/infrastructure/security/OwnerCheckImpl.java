package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.store.domain.QStore;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreRepository;
import com.suratgan.backend.user.application.UserMeService;
import com.suratgan.backend.user.application.dto.UserMeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OwnerCheckImpl implements OwnerCheck {
    private final StoreRepository storeRepository;
    private final UserMeService userMeService;

    @Override
    public boolean isOwner(UUID storeId) {
        if (storeId == null) return false;
        UserMeResponseDto userDetails = userMeService.getMe();

        UUID ownerId = userDetails.getId();
        if (ownerId == null) {
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
        return userMeService.getMe().getId();
    }

    @Override
    public String getOwnerName() {
        return userMeService.getMe().getNickname();
    }

    @Override
    public UUID getStoreId() {
        QStore store = QStore.store;
        Store item = storeRepository.findOne(store.owner.userId.eq(userMeService.getMe().getId())).orElse(null);
        return item == null ? null : item.getId().getId();
    }

    @Override
    public String getOwnerRole() {
        return userMeService.getMe().getRole().name();
    }
}
