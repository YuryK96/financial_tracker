package com.financial_tracker.core.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {


    Page<CategoryEntity> findByAccount_Id(UUID accountId, Pageable pageable);


    Optional<CategoryEntity> findByIdAndAccount_Id(UUID id, UUID accountId);


    Optional<CategoryEntity> findByNameAndAccount_Id(String name, UUID accountId);

    @EntityGraph(attributePaths = {
            "subcategories"
    })
    @Query("SELECT c FROM CategoryEntity c WHERE c.account.id = :accountId")
    Page<CategoryEntity> findByAccountIdWithSubcategories(UUID accountId, Pageable pageable);
}
