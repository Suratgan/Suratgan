package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.domain.service.RoleCheck;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 0. OWNER는 반드시 본인이 소유한 음식점의 음식만 등록, 수정, 삭제할 수 있다. MANAGER와 MASTER는 모든 음식에 접근이 가능하다.
 * 1. 음식의 숨김 처리는 조회 목록에서 보이지 않도록 처리한다.
 * 2. 음식 이미지가 없는 경우를 대비해 기본 이미지 URL을 제공한다.
 * 3. 음식 설명은 사용자가 직접 입력한 문구 또는 AI 생성 문구를 사용한다.
 * 4. 음식 삭제 시 물리적인 삭제 대신 soft delete로 구현한다.
 */

@Embeddable
@Getter
@ToString
@Table(name = "P_MENU")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {
    @Embedded
    private MenuId id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String menuInfo;

    @Column(nullable = false)
    private int price;

    @Column(length = 1000)
    private String menuImg;

    private boolean isDeleted;

    // 메뉴 생성(객체 생성)
    @Builder
    protected Menu(StoreId storeId, int menuIdx, String name, String menuInfo, int price, String menuImg) {
        this.id = new MenuId(menuIdx);
        this.name = name;
        this.menuInfo = menuInfo;
        this.price = price;
        this.menuImg = menuImg; // 기본 이미지 변경 필요
        this.isDeleted = false;
    }

    // 메뉴 삭제(soft delete)
    public void remove() {
        deletedAt = LocalDateTime.now();
    }

    // 숨김 처리 변경
    public void changeHidden() {
        isDeleted = !isDeleted;
    }

    // 상품 노출 가능 여부
    public boolean isVisible() {
        return (getDeletedAt() == null) && (!isDeleted);
    }

    // 음식 설명 문구 변경
    public void changeMenuInfo(String menuInfo) {
        this.menuInfo = menuInfo;
    }
}
