package com.financial_tracker.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

public record JwtRequest(

        @NotBlank
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
        String login,

        @NotBlank
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
        String password
) {
}
