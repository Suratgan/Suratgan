package com.suratgan.backend.store.application;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.StoreRepository;
import com.suratgan.backend.store.domain.dto.StoreDto;
import com.suratgan.backend.store.presentation.dto.MenuCreateRequestDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;

    @Transactional
    public ResponseEntity<String> create(UUID id, @Valid MenuCreateRequestDto request) {

        if (request == null) {
            return ResponseEntity.badRequest().body("요청 값이 비어있습니다.");
        }

        Store store = storeRepository.findById(StoreId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        StoreDto.MenuDto menuDto = StoreDto.MenuDto.builder()
                .name(request.getName())
                .menuInfo(request.getMenuInfo())
                .price(request.getPrice())
                .menuImg(request.getMenuImg())
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();

        store.createMenu(menuDto);

        return ResponseEntity.ok("메뉴 생성 완료");
    }
}
