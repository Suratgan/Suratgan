package com.suratgan.backend.category.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.domain.service.RoleCheck;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 0. 카테고리는 MANAGER, MASTER만 생성/수정/삭제가 가능하다.
 */

@Getter
@ToString
@Entity
@Table(name="P_CATEGORY")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @EmbeddedId
    private CategoryId id;

    @Column(name = "category_name")
    private String categoryName;

    @Builder
    public Category(UUID categoryId, String categoryName, RoleCheck roleCheck) {
        checkAuthority(roleCheck);

        this.id = (categoryId == null ? CategoryId.of() : CategoryId.of(categoryId));
        this.categoryName = categoryName;
    }

    // 카테고리명 수정
    public void change(String categoryName, RoleCheck roleCheck) {
        checkAuthority(roleCheck);

        this.categoryName = categoryName;
    }

    // 카테고리 삭제
    public void remove(RoleCheck roleCheck) {
        checkAuthority(roleCheck);

        deletedAt = LocalDateTime.now();
    }

    // 권한 체크
    private void checkAuthority(RoleCheck roleCheck) {
        if (!roleCheck.hasRole(List.of("OWNER", "MANAGER", "MASTER"))) {
            // 공통 Exception 로직 정의 후 추가
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
