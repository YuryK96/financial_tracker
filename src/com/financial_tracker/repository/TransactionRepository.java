package com.financial_tracker.repository;

import com.financial_tracker.domain.CategorizedTransaction;
import com.financial_tracker.domain.TransactionType;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface TransactionRepository {

    CategorizedTransaction save(CategorizedTransaction transaction);


    Boolean delete(UUID id);

    @Nullable
    CategorizedTransaction getById(UUID id);

    List<CategorizedTransaction> findByAccountId(UUID id);

    List<CategorizedTransaction> findByCategory(String categoryName);

    List<CategorizedTransaction> findBySubCategory(String subCategoryName);

    List<CategorizedTransaction> findByRange(LocalDate start, LocalDate end);


    List<CategorizedTransaction> findByType(TransactionType type);
}
