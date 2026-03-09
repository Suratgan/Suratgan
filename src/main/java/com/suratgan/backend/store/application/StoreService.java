package com.suratgan.backend.store.application;

import com.suratgan.backend.category.domain.Category;
import com.suratgan.backend.category.domain.CategoryRepository;
import com.suratgan.backend.category.domain.service.CategoryCheck;
import com.suratgan.backend.global.domain.service.AddressToCoords;
import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.StoreRepository;
import com.suratgan.backend.store.presentation.dto.StoreChangeRequestDto;
import com.suratgan.backend.store.presentation.dto.StoreCreateRequestDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final CategoryCheck categoryCheck;
    private final AddressToCoords addressToCoords;

    @Transactional
    public ResponseEntity<String> create(@Valid StoreCreateRequestDto request) {
        // role check 추가

        if (request == null) return ResponseEntity.badRequest().body("요청 목록이 비어있습니다.");

        // 음식점 중복 검사 로직
        Store store = storeRepository.findByStoreName(request.getName());
        if (store != null) ResponseEntity.badRequest().body("이미 존재하는 음식점 입니다.");

        // 카테고리 유효성 확인
        List<Category> categories = categoryRepository.findAllByCategoryNameIn(request.getCategories());
        if (categories.size() != request.getCategories().size()) {
            throw new IllegalArgumentException("일부 카테고리가 유효하지 않습니다.");
        }

        // 카테고리 변환
        List<UUID> categoryIds = categories.stream()
                .map(category -> category.getId().getId())
                .toList();

        // 음식점 생성
        Store newStore = Store.builder()
                .storeName(request.getName())
                .address(request.getAddress())
                .categoryIds(categoryIds)
                .addressToCoords(addressToCoords)
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .build();
        
        storeRepository.save(newStore);

        return ResponseEntity.ok("음식점 생성 완료");
    }

    @Transactional
    public ResponseEntity<String> change(UUID id, @Valid StoreChangeRequestDto request) {
        // role check 추가

        StoreId storeId = StoreId.of(id);
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        // 카테고리 유효성 확인
        List<Category> categories = categoryRepository.findAllByCategoryNameIn(request.getCategories());
        if (categories.size() != request.getCategories().size()) {
            throw new IllegalArgumentException("일부 카테고리가 유효하지 않습니다.");
        }

        // 카테고리 변환
        List<UUID> categoryIds = categories.stream()
                .map(category -> category.getId().getId())
                .toList();
        store.changeStore(request.getOwerName(), request.getStoreName(), request.getAddress(), categoryIds, addressToCoords, roleCheck, ownerCheck, categoryCheck);

        return ResponseEntity.ok("음식점 수정 완료");
    }

    @Transactional
    public ResponseEntity<String> remove(UUID id) {
        // role check 추가

        StoreId storeId = StoreId.of(id);
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        store.remove(roleCheck, ownerCheck);

        return ResponseEntity.ok("음식점 삭제 완료");
    }
}
