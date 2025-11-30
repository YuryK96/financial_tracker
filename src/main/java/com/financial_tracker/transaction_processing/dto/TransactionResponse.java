package com.financial_tracker.transaction_processing.dto;

import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        Currency currency,
        TransactionType type,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
