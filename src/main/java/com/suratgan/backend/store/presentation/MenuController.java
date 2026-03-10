package com.suratgan.backend.store.presentation;

import com.suratgan.backend.store.application.MenuQueryService;
import com.suratgan.backend.store.application.MenuService;
import com.suratgan.backend.store.application.dto.MenuResponseDto;
import com.suratgan.backend.store.presentation.dto.MenuCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}/menus")
public class MenuController {
    private final MenuService menuService;
    private final MenuQueryService menuQueryService;

    @Operation(summary = "메뉴 생성")
    @PostMapping
    public ResponseEntity<String> createMenu(@PathVariable("storeId") UUID id, @RequestBody @Valid MenuCreateRequestDto request) {
        return menuService.create(id, request);
    }

    @Operation(summary = "메뉴 수정")
    @PatchMapping("/{menuIdx}")
    public ResponseEntity<String> changeMenu(@PathVariable UUID storeId, @PathVariable int menuIdx, @RequestBody @Valid MenuCreateRequestDto request) {
        return menuService.change(storeId, menuIdx, request);
    }

    @Operation(summary = "메뉴 삭제")
    @DeleteMapping
    public ResponseEntity<String> removeMenu(@PathVariable UUID storeId, @RequestParam List<Integer> menuIdx) {
        return menuService.remove(storeId, menuIdx);
    }

    @Operation(summary = "메뉴 단건 조회")
    @GetMapping("/{menuIdx}")
    public MenuResponseDto searchMenu(@PathVariable UUID storeId, @PathVariable int menuIdx) {
        return menuQueryService.searchMenu(storeId, menuIdx);
    }

    @Operation(summary = "메뉴 목록 조회")
    @GetMapping
    public List<MenuResponseDto> searchMenus(@PathVariable UUID storeId) {
        return menuQueryService.searchMenus(storeId);
    }
}
