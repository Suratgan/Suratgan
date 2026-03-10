package com.suratgan.backend.store.presentation;

import com.suratgan.backend.store.application.MenuService;
import com.suratgan.backend.store.presentation.dto.MenuCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
