package com.suratgan.backend.store.application;

import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import com.suratgan.backend.store.application.dto.MenuResponseDto;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.query.StoreQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuQueryService {
    private final StoreQueryRepository storeQueryRepository;

    // 단건 조회
    public MenuResponseDto searchMenu(UUID storeId, int menuIdx) {
        return storeQueryRepository.findMenuByStoreIdAndMenuIdx(StoreId.of(storeId), menuIdx)
                .map(MenuResponseDto::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
    }

    // 목록 조회
    public List<MenuResponseDto> searchMenus(UUID storeId) {
        return storeQueryRepository.findAllMenusByStoreId(StoreId.of(storeId)).stream()
                .map(MenuResponseDto::from).toList();
    }
}
