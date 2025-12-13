package com.financial_tracker.auth.dto.request;

import org.jetbrains.annotations.NotNull;

public record JwtRefreshRequest(
        @NotNull
        String refreshToken
) {
}
