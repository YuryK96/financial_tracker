package com.financial_tracker.core.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    Optional<TransactionEntity> getByAccount_IdAndId(UUID accountId, UUID id);


    @Query("""
            SELECT t FROM TransactionEntity t
            LEFT JOIN FETCH t.subcategory
            LEFT JOIN FETCH t.source
            WHERE t.account.id = :accountId
            AND t.id = :id
            """)
    Optional<TransactionEntity> getByIdAndAccountIdWithSourceAndCategory(UUID accountId, UUID id);

    boolean existsByAccount_IdAndId(UUID accountId, UUID id);


    @Query("""
            SELECT t FROM TransactionEntity AS t
            LEFT JOIN FETCH t.subcategory
            LEFT JOIN FETCH t.source
            WHERE t.account.id = :accountId
            AND (:subcategoryId IS NULL OR t.subcategory.id = :subcategoryId)            
            AND (:categoryId IS NULL OR t.subcategory.category.id = :categoryId)         
            AND (:currency IS NULL OR t.currency = :currency)
            AND (:type IS NULL OR t.type = :type )    
            AND (CAST(CAST(:fromDate AS string ) AS timestamp) IS NULL OR t.createdAt >= CAST(CAST(:fromDate AS string) AS timestamp)    ) 
             AND (CAST(CAST(:toDate AS string ) AS timestamp) IS NULL OR t.createdAt <= CAST(CAST(:toDate AS string) AS timestamp))                     
            """)
    Page<TransactionEntity> findAllByFilters(
            @Param("accountId") UUID accountId,
            @Param("subcategoryId") UUID subcategoryId,
            @Param("categoryId") UUID categoryId,
            @Param("currency") Currency currency,
            @Param("type") TransactionType type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);


}
