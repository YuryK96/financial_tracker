package com.financial_tracker.auth.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.jetbrains.annotations.NotNull;

public record RegistrationRequest(

        @NotNull
        @Min(5)
        @Max(200)
        String login,

        @NotNull
        @Min(5)
        @Max(200)
        String password,

        @NotNull
        @Min(5)
        @Max(200)
        String accountName,

        @NotNull
        @Min(5)
        @Max(200)
        String checkPassword
) {

    private boolean passwordMatches() {
        return password != null && password.equals(checkPassword);
    }

    public RegistrationRequest {
        if (password != null && !password.equals(checkPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }
}
