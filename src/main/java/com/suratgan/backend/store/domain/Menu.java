package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 0. OWNER는 반드시 본인이 소유한 음식점의 음식만 등록, 수정, 삭제할 수 있다. MANAGER와 MASTER는 모든 음식에 접근이 가능하다.
 * 1. 음식의 숨김 처리는 조회 목록에서 보이지 않도록 처리한다.
 * 2. 음식 이미지가 없는 경우를 대비해 기본 이미지 URL을 제공한다.
 * 3. 음식 설명은 사용자가 직접 입력한 문구 또는 AI 생성 문구를 사용한다.
 * 4. 음식 삭제 시 물리적인 삭제 대신 soft delete로 구현한다.
 */

@Getter
@Entity
@Table(name = "P_MENU")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String menuInfo;

    @Column(nullable = false)
    private int price;

    @Column(length = 1000)
    private String menuImg;

    private boolean isDeleted;

    @Builder
    protected Menu(Long id, String name, String menuInfo, int price, String menuImg) {
        this.id = id;
        this.name = name;
        this.menuInfo = menuInfo;
        this.price = price;
        this.menuImg = menuImg;
        this.isDeleted = false;
    }
}
