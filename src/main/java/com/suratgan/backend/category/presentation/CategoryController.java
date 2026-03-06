package com.suratgan.backend.category.presentation;

import com.suratgan.backend.category.application.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
