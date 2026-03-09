package com.suratgan.backend.store.domain.query;

import com.suratgan.backend.store.domain.Menu;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.query.dto.StoreQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreQueryRepository {
    Optional<Store> findById(StoreId id); // 매장 한개조회
    Page<Store> findAll(StoreQueryDto.Search search, Pageable pageable); // 매장 검색
    Optional<Menu> findMenuByStoreIdAndMenuIdx(StoreId id, int menuIdx);
    List<Menu> findAllMenusByStoreId(StoreId id);
}
