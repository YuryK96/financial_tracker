package com.financial_tracker.account_management.dto.Request;

import jakarta.validation.constraints.Max;
import org.jetbrains.annotations.NotNull;

public record AccountCreate(
        @NotNull
        @Max(200)
        String name
) {
}
