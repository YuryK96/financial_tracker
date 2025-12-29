package com.financial_tracker.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record JwtRefreshRequest(
        @NotBlank
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String refreshToken
) {
}
