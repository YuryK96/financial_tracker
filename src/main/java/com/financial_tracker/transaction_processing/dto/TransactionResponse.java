package com.financial_tracker.transaction_processing.dto;

import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;
import com.financial_tracker.source_managment.dto.response.SourceResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        Currency currency,
        TransactionType type,
        UUID sourceId,
        String description,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        SourceResponse source,
        SubcategoryResponse subcategory
) {
}
