package com.suratgan.backend.store.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.domain.service.AddressToCoords;
import com.suratgan.backend.global.domain.service.OwnerCheck;
import com.suratgan.backend.global.domain.service.RoleCheck;
import com.suratgan.backend.store.domain.dto.StoreDto;
import com.suratgan.backend.store.domain.service.CategoryCheck;
import jakarta.persistence.*;
import lombok.*;
import org.apache.coyote.BadRequestException;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    private int reviewCnt;

    @Embedded
    private StoreLocation location;

    // 음식점 - 음식 관계
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_STORE_MENU", joinColumns = @JoinColumn(name="store_id"))
    @SQLRestriction("deleted_at IS NULL")
    @OrderColumn(name="menu_idx")
    private List<Menu> menus;

    // 음식점 - 카테고리 관계
    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="P_STORE_CATEGORY", joinColumns=@JoinColumn(name="store_id"))
    @SQLRestriction("deleted_at IS NULL")
    @OrderColumn(name="category_idx")
    private List<StoreCategory> categories;

    // 음식점 생성(카테고리는 생성과 동시에 설정)
    @Builder
    protected Store(UUID storeId, String storeName, String address, List<UUID> categoryIds, AddressToCoords addressToCoords, RoleCheck roleCheck, OwnerCheck ownerCheck, CategoryCheck categoryCheck) {
        checkAuthority(roleCheck, ownerCheck);

        if (ownerCheck.getStoreId() != null) {
            // 공통 Exception 로직 정의 후 추가
            //throw new BadRequestException("이미 보유한 매장이 있습니다.");
        }

        this.id = storeId == null ? StoreId.of() : StoreId.of(storeId);
        this.owner = new Owner(ownerCheck.getOwnerId(), ownerCheck.getOwnerRole(), ownerCheck.getOwnerName());
        this.storeName = storeName;
        this.rating = 0;
        this.reviewCnt = 0;
        this.location = new StoreLocation(address, addressToCoords);

        // 카테고리 설정
        createCategory(StoreDto.CategoryDto
                .builder()
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .categoryIds(categoryIds)
                .build());
    }

    // 음식점 수정
    public void changeStore(String ownerName, String storeName, String address, AddressToCoords addressToCoords, RoleCheck roleCheck, OwnerCheck ownerCheck) {
        checkAuthority(roleCheck, ownerCheck);

        this.owner = new Owner(ownerCheck.getOwnerId(), ownerCheck.getOwnerRole(), ownerName);
        this.storeName = storeName;
        this.location = new StoreLocation(address, addressToCoords);
    }

    // 음식점 삭제(Soft Delete)
    public void remove(RoleCheck roleCheck, OwnerCheck ownerCheck) {
        checkAuthority(roleCheck, ownerCheck);

        deletedAt = LocalDateTime.now();

        // 음식 삭제
        if (menus != null) {
            menus.forEach(Menu::remove);
        }

        // 카테고리 삭제
        if (categories != null) {
            categories.forEach(StoreCategory::remove);
        }
    }

    // 주소 거리 계산
    // 외부 구현 기술이 필요할 것으로 예상되어 인터페이스 생성 고려

    // 리뷰 발생 시 평점 관련 이벤트 핸들러 필요


    // 음식(MENU)
    // 음식 생성

    // 음식 수정

    // 음식 삭제
    

    // 카테고리(CATEGORY)
    // 카테고리 생성
    public void createCategory(StoreDto.CategoryDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        List<UUID> categoryIds = dto.getCategoryIds();
        if (categoryIds == null || categoryIds.isEmpty()) return;

        // 분류 유효성 검사
        if (!dto.getCategoryCheck().exists(categoryIds)) {
            // 공통 Exception 로직 정의 후 추가
            //throw new InvalidCategoryException("유효하지 않은 카테고리가 포함되어 있습니다.");
        }

        // 카테고리가 비어있으면 기본 리스트 생성하여 반환
        categories = Objects.requireNonNullElseGet(categories, ArrayList::new);

        // 카테고리 아이디 리스트로 구성하여 추가
        categories.addAll(categoryIds.stream().distinct().map(StoreCategory::new).toList());
    }
    
    // 카테고리 수정
    
    // 카테고리 삭제

    // 권한 체크
    public void checkAuthority(RoleCheck roleCheck, OwnerCheck ownerCheck) {

        // 관리자 권한인 경우 통과
        if (roleCheck.hasRole(List.of("MANAGER", "MASTER"))) {
            return;
        }

        // 신규 등록인 경우라면 OWNER 권한 확인
        if (id == null) {
            if (!roleCheck.hasRole("OWNER")) {
                // 공통 Exception 로직 정의 후 추가
                //throw new UnAuthorizedException();
            }
        } else if (!ownerCheck.isOwner(id.getId())) { // 상점 정보 수정인 경우 매장 소유주 확인
            // 공통 Exception 로직 정의 후 추가
            //throw new UnAuthorizedException();
        }
    }
}
