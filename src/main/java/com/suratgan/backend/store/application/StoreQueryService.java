package com.suratgan.backend.store.application;

import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import com.suratgan.backend.store.application.dto.StoreResponseDto;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.query.StoreQueryRepository;
import com.suratgan.backend.store.presentation.dto.StoreSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreQueryService {
    private final StoreQueryRepository storeQueryRepository;

    // 단건 조회
    public StoreResponseDto searchStore(UUID storeId) {
        return storeQueryRepository.findById(StoreId.of(storeId))
                .map(StoreResponseDto::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    // 목록 조회
    public Page<StoreResponseDto> searchStores(StoreSearchDto request, Pageable pageable) {
        return storeQueryRepository.findAll(request, pageable)
                .map(StoreResponseDto::from);
    }
}
