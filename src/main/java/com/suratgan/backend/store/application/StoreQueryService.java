package com.suratgan.backend.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreQueryService {
//    private final StoreQueryRepository storeQueryRepository;
//
//    // 단건 조회
//    public StoreResponseDto getStore(UUID storeId) {
//        return storeQueryRepository.findById(StoreId.of(storeId))
//                .map(StoreResponseDto::from)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));
//    }
//
//    // 목록 조회
//    public Page<StoreResponseDto> searchStores(StoreQueryDto.Search search, Pageable pageable) {
//        return storeQueryRepository.findAll(search, pageable)
//                .map(StoreResponseDto::from);
//    }
}
