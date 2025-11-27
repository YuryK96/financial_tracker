package com.financial_tracker.core.subcategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, UUID> {

}
