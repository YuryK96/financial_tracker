package com.financial_tracker.category_management.dto.request;

import jakarta.validation.constraints.Max;
import org.jetbrains.annotations.NotNull;

public record CategoryUpdate(
        @NotNull
        @Max(200)
        String name
) {
}
