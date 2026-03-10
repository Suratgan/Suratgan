package com.suratgan.backend.store.application;

import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.MenuId;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.StoreRepository;
import com.suratgan.backend.store.domain.dto.StoreDto;
import com.suratgan.backend.store.domain.service.MenuDescriptionGenerate;
import com.suratgan.backend.store.presentation.dto.MenuCreateRequestDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final MenuDescriptionGenerate menuDescriptionGenerate;

    @Transactional
    public ResponseEntity<String> create(UUID storeId, @Valid MenuCreateRequestDto request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("요청 값이 비어있습니다.");
        }

        Store store = storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        String menuInfo = request.isAiGenerated() ?
                menuDescriptionGenerate.generate(request.getName(), request.getMenuInfo()) : request.getMenuInfo();

        StoreDto.MenuDto menuDto = StoreDto.MenuDto.builder()
                .name(request.getName())
                .menuInfo(menuInfo)
                .price(request.getPrice())
                .menuImg(request.getMenuImg())
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();

        store.createMenu(menuDto);

        return ResponseEntity.ok("메뉴 생성 완료");
    }

    @Transactional
    public ResponseEntity<String> change(UUID storeId, int menuIdx, @Valid MenuCreateRequestDto request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("요청 값이 비어있습니다.");
        }

        Store store = storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        String menuInfo = request.isAiGenerated() ?
                menuDescriptionGenerate.generate(request.getName(), request.getMenuInfo()) : request.getMenuInfo();

        StoreDto.MenuDto menuDto = StoreDto.MenuDto.builder()
                .name(request.getName())
                .menuInfo(menuInfo)
                .price(request.getPrice())
                .menuImg(request.getMenuImg())
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();

        store.changeMenu(MenuId.of(menuIdx), menuDto);

        return ResponseEntity.ok("메뉴 수정 완료");
    }

    @Transactional
    public ResponseEntity<String> remove(UUID storeId, List<Integer> menuIds) {
        Store store = storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        store.removeMenu(roleCheck, ownerCheck, menuIds);

        return ResponseEntity.ok("메뉴 삭제 완료");
    }

    @Transactional
    public ResponseEntity<String> hiddenMenu(UUID storeId, int menuIdx, boolean hidden) {
        Store store = storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        store.getMenu(MenuId.of(menuIdx)).changeHidden(hidden);

        return ResponseEntity.ok("숨김 상태 변경 완료");
    }
}
