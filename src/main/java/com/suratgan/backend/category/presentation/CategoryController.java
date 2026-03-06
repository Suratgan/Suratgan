package com.suratgan.backend.category.presentation;

import com.suratgan.backend.category.application.CategoryService;
import com.suratgan.backend.category.presentation.dto.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * User 작업 후 UserPrincipal을 이용한 User 정보 및 권한 검사 필요
 * @AuthenticationPrincipal UserPrincipal user
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {
    CategoryService categoryService;

    @Operation(summary = "카테고리 생성")
    @PostMapping
    public ResponseEntity<String> createCategories(@RequestBody @Valid List<String> categories) {
        return categoryService.create(categories);
    }

    @Operation(summary = "카테고리 수정")
    @PatchMapping
    public ResponseEntity<String> changeCategories(@RequestBody @Valid List<CategoryDto> request) {
        return categoryService.change(request);
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping
    public ResponseEntity<String> removeCategories(@RequestBody @Valid List<UUID> ids) {
        return categoryService.remove(ids);
    }
}
