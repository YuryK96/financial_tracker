package com.financial_tracker.transaction_processing.dto.request;

import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionCreate(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Currency currency,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        TransactionType type,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        UUID subcategoryId,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        UUID sourceId,

        @Nullable
        String description
) {
}