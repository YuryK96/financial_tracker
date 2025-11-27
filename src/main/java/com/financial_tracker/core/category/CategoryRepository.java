package com.financial_tracker.core.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

}
