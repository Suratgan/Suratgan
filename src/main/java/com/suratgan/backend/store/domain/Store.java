package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.domain.service.AddressToCoords;
import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.dto.StoreDto;
import com.suratgan.backend.store.domain.exception.MenuNotFoundException;
import com.suratgan.backend.category.domain.service.CategoryCheck;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 0. 각 음식점은 OWNER 이상의 권한을 가진 사용자 중 반드시 하나의 사용자와 연결되어 관리 권한이 부여된다.
 * 1. 음식점은 위도, 경도 정보를 포함하여 사용자 주소와의 거리 계산 시 사용된다.
 * 2. 음식점은 하나 이상의 카테고리에 속해야 한다.
 * 3. 음식점의 리뷰 수와 평점은 리뷰 서비스와의 일관성이 유지되어야 한다.
 * (리뷰 작성 시 발생하는 이벤트를 구독하여 평점과 리뷰 수 업데이트 필요)
 * 4. 음식점 삭제 시 물리적인 삭제 대신 soft delete로 구현한다.
 */

@Getter
@ToString
@Entity
@Table(name="P_STORE")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {
    @EmbeddedId
    private StoreId id;

    @Embedded
    private Owner owner;

    private String storeName;
    private double rating;
    private double totalRating;
    private long reviewCnt;

    @Embedded
    private StoreLocation location;

    // 음식점 - 음식 관계
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_MENU", joinColumns = @JoinColumn(name="store_id"))
    @OrderColumn(name="menu_orders")
    private List<Menu> menus;

    // 음식점 - 카테고리 관계
    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="P_STORE_CATEGORY", joinColumns = @JoinColumn(name = "store_id", referencedColumnName = "id"))
    @OrderColumn(name="category_orders")
    private List<StoreCategory> categories;

    // 음식점 생성(카테고리는 생성과 동시에 설정)
    @Builder
    protected Store(UUID storeId, String storeName, String address, List<UUID> categoryIds, AddressToCoords addressToCoords, RoleCheck roleCheck, OwnerCheck ownerCheck, CategoryCheck categoryCheck) {
        checkAuthority(roleCheck, ownerCheck);

        if (ownerCheck.getStoreId() != null) {
            // 공통 Exception 로직 정의 후 추가
            throw new IllegalArgumentException("이미 보유한 매장이 있습니다.");
        }

        // 카테고리 설정
        createCategory(StoreDto.CategoryDto
                .builder()
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .categoryIds(categoryIds)
                .build());

        this.id = storeId == null ? StoreId.of() : StoreId.of(storeId);
        this.owner = new Owner(ownerCheck.getOwnerId(), ownerCheck.getOwnerRole(), ownerCheck.getOwnerName());
        this.storeName = storeName;
        this.rating = 0;
        this.totalRating = 0;
        this.reviewCnt = 0;
        this.location = new StoreLocation(address, addressToCoords);
    }

    // 음식점 수정
    public void changeStore(String ownerName, String storeName, String address, List<UUID> categoryIds, AddressToCoords addressToCoords, RoleCheck roleCheck, OwnerCheck ownerCheck, CategoryCheck categoryCheck) {
        checkAuthority(roleCheck, ownerCheck);

        if (ownerName != null) this.owner = new Owner(ownerCheck.getOwnerId(), ownerCheck.getOwnerRole(), ownerName);
        if (storeName != null) this.storeName = storeName;
        if (address != null) this.location = new StoreLocation(address, addressToCoords);

        // 카테고리 수정
        changeCategory(StoreDto.CategoryDto
                .builder()
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .categoryIds(categoryIds)
                .build());
    }

    // 음식점 삭제(Soft Delete)
    public void remove(RoleCheck roleCheck, OwnerCheck ownerCheck) {
        checkAuthority(roleCheck, ownerCheck);

        deletedAt = LocalDateTime.now();

        // 음식 삭제
        if (menus != null) {
            menus.clear();
        }

        // 카테고리 삭제
        if (categories != null) {
            categories.clear();
        }
    }

    // 리뷰 발생 시 카운트 증가 및 평점 반영
    public void addReview(double newRating) {
        reviewCnt++;
        totalRating += newRating;

        // 소수점 한 자리까지만 반올림
        rating = Math.round(totalRating / reviewCnt * 10) / 10.0;
    }

    // 리뷰 삭제 시 카운트 감소 및 평점 반영
    public void removeReview(double removeRating) {
        if (reviewCnt <= 0) {
            rating = 0.0;
            return;
        }

        reviewCnt--;
        totalRating -= removeRating;
        rating = Math.round(totalRating / reviewCnt * 10) / 10.0;
    }

    // 음식(MENU)
    // 음식 생성
    @Transactional
    public void createMenu(StoreDto.MenuDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        // 음식이 비어있으면 기본 리스트 생성하여 반환
        menus = Objects.requireNonNullElseGet(menus, ArrayList::new);

        // 음식 리스트에 추가
        menus.add(StoreDto.toMenu(id, menus.size(), dto));
    }

    // 음식 조회
    public Menu getMenu(MenuId menuId) {
        if (menuId == null) return null;
        return menus.stream().filter(m -> m.getMenuId().equals(menuId)).findFirst().orElse(null);
    }

    // 음식 수정
    public void changeMenu(MenuId menuId, StoreDto.MenuDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        Menu menu = Optional.ofNullable(getMenu(menuId))
                .orElseThrow(MenuNotFoundException::new);
        int idx = menu.getMenuId().getMenuIdx();

        menus.set(idx, StoreDto.toMenu(id, idx, dto));
    }

    // 음식 삭제
    public void removeMenu(RoleCheck roleCheck, OwnerCheck ownerCheck, List<Integer> menuIds) {
        checkAuthority(roleCheck, ownerCheck);

        if (menus == null || menuIds.isEmpty()) return;

        // 삭제할 음식 아이디를 타겟으로 설정
        Set<Integer> targetIds = new HashSet<>(menuIds);

        // 삭제되지 않은 음식 중 타겟에 해당하는 음식 삭제
        menus.removeIf(c -> targetIds.contains(c.getMenuId().getMenuIdx()));
    }

    // 카테고리(CATEGORY)
    // 카테고리 생성
    public void createCategory(StoreDto.CategoryDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        List<UUID> categoryIds = dto.getCategoryIds();
        if (categoryIds == null || categoryIds.isEmpty()) return;

        // 카테고리 유효성 검사
        if (!dto.getCategoryCheck().exists(categoryIds)) {
            // 공통 Exception 로직 정의 후 추가
            throw new IllegalArgumentException("유효하지 않은 카테고리가 포함되어 있습니다.");
        }

        // 카테고리가 비어있으면 기본 리스트 생성하여 반환
        categories = Objects.requireNonNullElseGet(categories, ArrayList::new);

        // 카테고리 아이디 리스트로 구성하여 추가
        categories.addAll(categoryIds.stream().distinct().map(StoreCategory::new).toList());
    }
    
    // 카테고리 수정
    public void changeCategory(StoreDto.CategoryDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        // 카테고리 유효성 검사
        if (!dto.getCategoryCheck().exists(dto.getCategoryIds())) {
            // 공통 Exception 로직 정의 후 추가
            throw new IllegalArgumentException("유효하지 않은 카테고리가 포함되어 있습니다.");
        }

        // 기존 카테고리 비우고 새로 생성
        if (categories != null) categories.clear();
        createCategory(dto);
    }
    
    // 카테고리 삭제
    public void removeCategory(StoreDto.CategoryDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        if (categories == null || dto.getCategoryIds() == null) return;

        // 삭제할 카테고리 아이디를 타겟으로 설정
        Set<UUID> targetIds = new HashSet<>(dto.getCategoryIds());

        // 삭제되지 않은 카테고리 중 타겟에 해당하는 카테고리를 해당 음식점 카테고리에서 삭제
        categories.removeIf(c -> targetIds.contains(c.getCategoryId()));
    }

    // 권한 체크
    public void checkAuthority(RoleCheck roleCheck, OwnerCheck ownerCheck) {

        // 관리자 권한인 경우 통과
        if (roleCheck.hasRole(List.of("OWNER", "MANAGER", "MASTER"))) {
            return;
        }

        // 신규 등록인 경우라면 OWNER 권한 확인
        if (id == null) {
            if (!roleCheck.hasRole("OWNER")) {
                // 공통 Exception 로직 정의 후 추가
                throw new IllegalArgumentException();
            }
        } else if (!ownerCheck.isOwner(id.getId())) { // 상점 정보 수정인 경우 매장 소유주 확인
            // 공통 Exception 로직 정의 후 추가
            throw new IllegalArgumentException();
        }
    }
}
