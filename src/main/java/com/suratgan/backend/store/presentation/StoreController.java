package com.suratgan.backend.store.presentation;

import com.suratgan.backend.store.application.StoreService;
import com.suratgan.backend.store.presentation.dto.StoreChangeRequestDto;
import com.suratgan.backend.store.presentation.dto.StoreCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "음식점 생성")
    @PostMapping
    public ResponseEntity<String> createStore(@RequestBody @Valid StoreCreateRequestDto request) {
        return storeService.create(request);
    }

    @Operation(summary = "음식점 수정")
    @PatchMapping("/{storeId}")
    public ResponseEntity<String> changeStore(@PathVariable("storeId") UUID id, @RequestBody @Valid StoreChangeRequestDto request) {
        return storeService.change(id, request);
    }

    @Operation(summary = "음식점 삭제")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> removeStore(@PathVariable("storeId") UUID id) {
        return storeService.remove(id);
    }
}
