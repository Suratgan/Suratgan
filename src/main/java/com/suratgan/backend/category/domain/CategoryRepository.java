package com.suratgan.backend.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
    boolean existsByCategoryNameIn(List<String> categories);
    List<Category> findById_IdIn(List<UUID> ids);
}
