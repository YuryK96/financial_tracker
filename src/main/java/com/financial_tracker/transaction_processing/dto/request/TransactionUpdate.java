package com.financial_tracker.transaction_processing.dto.request;

import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionUpdate(
        @NotNull
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull
        Currency currency,

        @NotNull
        TransactionType type,

        @NotNull
        UUID subcategoryId
) {
}