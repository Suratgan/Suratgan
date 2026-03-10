package com.suratgan.backend.store.presentation;

import com.suratgan.backend.store.application.StoreQueryService;
import com.suratgan.backend.store.application.StoreService;
import com.suratgan.backend.store.application.dto.StoreResponseDto;
import com.suratgan.backend.store.presentation.dto.StoreChangeRequestDto;
import com.suratgan.backend.store.presentation.dto.StoreCreateRequestDto;
import com.suratgan.backend.store.presentation.dto.StoreSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {
    private final StoreService storeService;
    private final StoreQueryService storeQueryService;

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

    @Operation(summary = "음식점 단건 조회")
    @GetMapping("/{storeId}")
    public StoreResponseDto searchStore(@PathVariable("storeId") UUID id) {
        return storeQueryService.searchStore(id);
    }

    @Operation(summary = "음식점 목록 조회")
    @GetMapping
    public Page<StoreResponseDto> searchStores(@RequestParam(defaultValue = "0") int page, @ModelAttribute StoreSearchDto request) {
        Pageable pageable = PageRequest.of(page, 10);
        return storeQueryService.searchStores(request, pageable);
    }
}
