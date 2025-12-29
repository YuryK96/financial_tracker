package com.financial_tracker.transaction_processing.dto.request;

import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionUpdate(
        JsonNullable<@Positive(message = "Amount must be positive") @NotNull BigDecimal> amount,

        JsonNullable<@NotNull Currency> currency,

        JsonNullable<@NotNull TransactionType> type,

        JsonNullable<@NotNull UUID> subcategoryId,
        JsonNullable<@NotNull UUID> sourceId,

        JsonNullable<String> description
) {


}