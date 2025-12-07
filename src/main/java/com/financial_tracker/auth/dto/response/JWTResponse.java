package com.financial_tracker.auth.dto.response;

import org.jetbrains.annotations.NotNull;

public record JWTResponse(
        @NotNull
        String token
) {
}
