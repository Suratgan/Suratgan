package com.suratgan.backend.store.infrastructure.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suratgan.backend.store.domain.Menu;
import com.suratgan.backend.store.domain.QStore;
import com.suratgan.backend.store.domain.Store;
import com.suratgan.backend.store.domain.StoreId;
import com.suratgan.backend.store.domain.query.StoreQueryRepository;
import com.suratgan.backend.store.domain.query.dto.StoreQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    // 음식점 조회
    public Optional<Store> findById(StoreId id) {
        QStore store = QStore.store;

        return Optional.ofNullable(queryFactory
                .selectFrom(store)
                .where(store.id.eq(id))
                .fetchOne());
    }

    @Override
    // 음식점 전체 조회
    public Page<Store> findAll(StoreQueryDto.Search search, Pageable pageable) {
        QStore store = QStore.store;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(store.deletedAt.isNull());

        // 거리 기반 필터링
        NumberExpression<Double> distanceMeter = null;
        if (search.getLongitude() != null && search.getLatitude() != null) {
            String userPoint = "POINT(%.10f %.10f)".formatted(search.getLongitude(), search.getLatitude());
            distanceMeter = Expressions.numberTemplate(Double.class,
                    "ST_DistanceSphere(ST_SetSRID(ST_MakePoint({0}, {1}), 4326), ST_GeomFromText({2}, 4326))",
                    store.location.longitude, store.location.latitude, userPoint);

            builder.and(distanceMeter.loe(10000.0)); // 10km
        }

        // 매장명
        if (StringUtils.hasText(search.getStoreName())) {
            builder.and(store.storeName.containsIgnoreCase(search.getStoreName()));
        }

        // 카테고리
        if (search.getCategoryIds() != null && !search.getCategoryIds().isEmpty()) {
            builder.and(store.categories.any().categoryId.in(search.getCategoryIds()));
        }

        // 데이터 조회
        List<Store> contents = queryFactory
                .selectFrom(store)
                .distinct()
                .leftJoin(store.categories).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(store.createdAt.desc())
                .fetch();

        // 카운트
        JPAQuery<Long> countQuery = queryFactory
                .select(store.count())
                .from(store)
                .where(builder);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    // 음식점의 메뉴 단건 조회
    public Optional<Menu> findMenuByStoreIdAndMenuIdx(StoreId id, int menuIdx) {
        QStore store = QStore.store;

        Store resultStore = queryFactory
                .selectFrom(store)
                .leftJoin(store.menus).fetchJoin()
                .where(store.id.eq(id))
                .fetchOne();

        if (resultStore == null) return Optional.empty();

        return resultStore.getMenus().stream()
                .filter(m -> m.getMenuId().getMenuIdx() == menuIdx)
                .findFirst();
    }

    @Override
    // 음식점의 메뉴 전체 조회
    public List<Menu> findAllMenusByStoreId(StoreId id) {
        QStore store = QStore.store;

        Store resultStore = queryFactory
                .selectFrom(store)
                .leftJoin(store.menus).fetchJoin()
                .where(store.id.eq(id))
                .fetchOne();

        return resultStore == null ? Collections.emptyList() : resultStore.getMenus();
    }
}
