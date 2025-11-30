package com.financial_tracker.core.subcategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, UUID> {

    Page<SubcategoryEntity> findByCategory_IdAndCategory_Account_Id(
            UUID categoryId,
            UUID accountId,
            Pageable pageable
    );

    Optional<SubcategoryEntity> findByIdAndCategory_Account_Id(
           UUID id,UUID accountId
    );


    boolean existsByIdAndCategory_Account_Id(UUID id, UUID categoryAccountId);
}
