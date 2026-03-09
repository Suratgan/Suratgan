package com.suratgan.backend.store.application;

import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreRepository;
import com.suratgan.backend.store.presentation.dto.StoreRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private static StoreRepository storeRepository;
    private static RoleCheck roleCheck;

    public ResponseEntity<String> create(@Valid StoreRequestDto request) {
        if (request == null) return ResponseEntity.badRequest().body("요청 목록이 비어있습니다.");

        // 음식점 중복 검사 로직

        // 음식점 생성
        Store store = Store.builder()
                .storeName(request.getName())
                .build();
        
        storeRepository.save(store);

        return ResponseEntity.ok("음식점 생성 완료");
    }
}
