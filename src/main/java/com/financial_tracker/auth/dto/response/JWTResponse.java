package com.financial_tracker.auth.dto.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record JWTResponse(
        @NotNull
        String accessToken,
        @Nullable
        String refreshToken
) {
}
