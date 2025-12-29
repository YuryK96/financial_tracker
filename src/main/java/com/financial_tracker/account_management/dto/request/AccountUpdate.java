package com.financial_tracker.account_management.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

public record AccountUpdate(
        @NotBlank
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name
) {
}
