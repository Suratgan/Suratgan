package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 0. 각 음식점은 OWNER 이상의 권한을 가진 사용자 중 반드시 하나의 사용자와 연결되어 관리 권한이 부여된다.
 * 1. 음식점은 위도, 경도 정보를 포함하여 사용자 주소와의 거리 계산 시 사용된다.
 * 2. 음식점은 하나 이상의 카테고리에 속해야 한다.
 * 3. 음식점의 리뷰 수와 평점은 리뷰 서비스와의 일관성이 유지되어야 한다.
 * (리뷰 작성 시 발생하는 이벤트를 구독하여 평점과 리뷰 수 업데이트 필요)
 * 4. 음식점 삭제 시 물리적인 삭제 대신 soft delete로 구현한다.
 */

@Entity
@Getter
@Table(name="P_STORE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Owner owner;

    private String storeName;
    private double rating;
    private int reviewCnt;

    private StoreLocation location;

    // 음식점 - 음식 관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "store_id")
    private List<Menu> menus;

    // 음식점 - 카테고리 관계

}
