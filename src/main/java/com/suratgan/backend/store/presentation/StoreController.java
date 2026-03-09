package com.suratgan.backend.store.presentation;

import com.suratgan.backend.store.application.StoreService;
import com.suratgan.backend.store.presentation.dto.StoreRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class StoreController {
    private static StoreService storeService;

    @Operation(summary = "카테고리 생성")
    @PostMapping
    public ResponseEntity<String> createStore(@RequestBody @Valid StoreRequestDto request) {
        return storeService.create(request);
    }
}
