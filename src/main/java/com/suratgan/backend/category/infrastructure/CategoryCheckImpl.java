package com.suratgan.backend.category.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suratgan.backend.category.domain.QCategory;
import com.suratgan.backend.category.domain.service.CategoryCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryCheckImpl implements CategoryCheck {
    private final JPAQueryFactory queryFactory;

    @Override
    // 등록 또는 수정하려는 카테고리가 존재하는지 확인
    public boolean exists(List<UUID> categoryIds) {
        QCategory category = QCategory.category;
        if (categoryIds == null || categoryIds.isEmpty()) return true;

        /**
         * SELECT COUNT(category.id)
         * FROM category
         * WHERE category.id.id IN (?, ?, ?) -- categoryIds의 내용들
         */
        List<UUID> ids = categoryIds.stream().distinct().toList();
        long count = Objects.requireNonNullElse(queryFactory
                .select(category.count())
                .from(category)
                .where(category.id.id.in(categoryIds))
                .fetchOne(), 0L);

        return count == ids.size();
    }
}
