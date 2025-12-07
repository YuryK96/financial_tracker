package com.financial_tracker.auth.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.jetbrains.annotations.NotNull;

public record JwtRequest(

        @NotNull
        @Min(5)
        @Max(200)
        String login,

        @NotNull
        @Min(5)
        @Max(200)
        String password
) {
}
