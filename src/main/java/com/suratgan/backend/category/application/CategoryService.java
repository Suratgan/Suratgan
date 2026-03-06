package com.suratgan.backend.category.application;

import com.suratgan.backend.category.domain.Category;
import com.suratgan.backend.category.domain.CategoryId;
import com.suratgan.backend.category.domain.CategoryRepository;
import com.suratgan.backend.category.presentation.dto.CategoryDto;
import com.suratgan.backend.global.domain.service.RoleCheck;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Transactional
    public ResponseEntity<String> update(@Valid List<CategoryDto> request) {
        if (request == null || request.isEmpty())
            ResponseEntity.badRequest().body("요청 목록이 비어있습니다.");

        // request로 map 생성
        Map<UUID, String> categoryMap = Objects.requireNonNull(request).stream()
                .collect(Collectors.toMap(CategoryDto::getId, CategoryDto::getCategory));

        List<UUID> uuids = request.stream().map(CategoryDto::getId).toList();
        List<Category> categories = categoryRepository.findById_IdIn(uuids);

        categories.forEach(category -> {
            String categoryName = categoryMap.get(category.getId().getId());
            category.change(categoryName, roleCheck);
        });

        return ResponseEntity.ok("카테고리 수정 완료");
    }
}
