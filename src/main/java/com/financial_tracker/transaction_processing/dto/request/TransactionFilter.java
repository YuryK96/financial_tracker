package com.financial_tracker.transaction_processing.dto.request;

import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionFilter(
        LocalDateTime fromDate,
        LocalDateTime toDate,
        UUID subcategoryId,
        UUID categoryId,
        Currency currency,
        TransactionType type


) {
    public TransactionFilter {
        if (fromDate != null && toDate != null) {
            if (fromDate.isAfter(toDate)) {
                throw new IllegalArgumentException("From date must be before to date");
            }
        }


    }

}