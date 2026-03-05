package com.suratgan.backend.category.application;

import com.suratgan.backend.category.domain.Category;
import com.suratgan.backend.category.domain.CategoryRepository;
import com.suratgan.backend.global.domain.service.RoleCheck;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final RoleCheck roleCheck;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<String> create(@Valid List<String> categories) {
        if (categories == null || categories.isEmpty())
            ResponseEntity.badRequest().body("카테고리 리스트의 값이 존재하지 않습니다.");

        // 카테고리 중복 검사 로직
        categories = Objects.requireNonNull(categories).stream().distinct().toList();
        if (categoryRepository.existsByCategoryNameIn(categories))
            ResponseEntity.badRequest().body("이미 존재하는 카테고리가 포함되어 있습니다.");

        List<Category> items = categories.stream()
                .map(name -> Category.builder()
                        .categoryName(name).roleCheck(roleCheck).build()).toList();
        categoryRepository.saveAll(items);

        return ResponseEntity.ok("카테고리 생성 완료");
    }
}
