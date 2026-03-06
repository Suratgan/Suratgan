package com.suratgan.backend.category.application;

import com.suratgan.backend.category.domain.Category;
import com.suratgan.backend.category.domain.CategoryRepository;
import com.suratgan.backend.category.presentation.dto.CategoryRequestDto;
import com.suratgan.backend.category.presentation.dto.CategoryResponseDto;
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
            return ResponseEntity.badRequest().body("카테고리 리스트의 값이 존재하지 않습니다.");

        // 카테고리 중복 검사 로직
        categories = Objects.requireNonNull(categories).stream().distinct().toList();
        if (categoryRepository.existsByCategoryNameIn(categories))
            return ResponseEntity.badRequest().body("이미 존재하는 카테고리가 포함되어 있습니다.");

        List<Category> items = categories.stream()
                .map(name -> Category.builder()
                        .categoryName(name).roleCheck(roleCheck).build()).toList();
        categoryRepository.saveAll(items);

        return ResponseEntity.ok("카테고리 생성 완료");
    }

    @Transactional
    public ResponseEntity<String> change(@Valid List<CategoryRequestDto> request) {
        if (request == null || request.isEmpty())
            return ResponseEntity.badRequest().body("수정 요청 목록이 비어있습니다.");

        // request로 map 생성
        Map<UUID, String> categoryMap = Objects.requireNonNull(request).stream()
                .collect(Collectors.toMap(CategoryRequestDto::getId, CategoryRequestDto::getCategory));

        List<UUID> uuids = request.stream().map(CategoryRequestDto::getId).toList();
        List<Category> categories = getCategories(uuids);

        categories.forEach(c -> {
            String categoryName = categoryMap.get(c.getId().getId());
            c.change(categoryName, roleCheck);
        });

        return ResponseEntity.ok("카테고리 수정 완료");
    }

    @Transactional
    public ResponseEntity<String> remove(@Valid List<UUID> ids) {
        if (ids == null || ids.isEmpty())
            return ResponseEntity.badRequest().body("삭제 요청 목록이 비어있습니다.");

        List<Category> categories = getCategories(ids);
        categories.forEach(c -> c.remove(roleCheck));

        return ResponseEntity.ok("카테고리 삭제 완료");
    }

    private List<Category> getCategories(List<UUID> ids) {
        List<Category> categories = categoryRepository.findById_IdIn(ids);

        if (categories.size() != ids.size())
            throw new IllegalArgumentException("요청한 카테고리가 존재하지 않습니다.");

        return categories;
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(c -> CategoryResponseDto.builder()
                        .id(c.getId().getId())
                        .category(c.getCategoryName())
                        .build()).toList();
    }
}
