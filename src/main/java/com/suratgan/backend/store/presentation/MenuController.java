package com.suratgan.backend.store.presentation;

import com.suratgan.backend.store.application.MenuService;
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
    @DeleteMapping("/{menuIdx}")
    public ResponseEntity<String> removeMenu(@PathVariable UUID storeId, @PathVariable List<Integer> menuIdx) {
        return menuService.remove(storeId, menuIdx);
    }
}
