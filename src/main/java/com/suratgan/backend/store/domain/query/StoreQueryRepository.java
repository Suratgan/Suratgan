package com.suratgan.backend.store.domain.query;

import com.suratgan.backend.store.domain.Menu;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.presentation.dto.StoreSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StoreQueryRepository {
    Optional<Store> findById(StoreId id); // 매장 한개조회
    Page<Store> findAll(StoreSearchDto request, Pageable pageable); // 매장 검색
    Optional<Menu> findMenuByStoreIdAndMenuIdx(StoreId id, int menuIdx);
    List<Menu> findAllMenusByStoreId(StoreId id);
}
