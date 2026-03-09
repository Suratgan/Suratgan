package com.suratgan.backend.store.domain;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StoreRepository extends JpaRepository<Store, StoreId>, QuerydslPredicateExecutor<Store> {

    Store findByStoreName(@NotBlank String name);
}
