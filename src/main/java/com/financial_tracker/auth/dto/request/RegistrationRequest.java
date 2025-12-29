package com.financial_tracker.auth.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

public record RegistrationRequest(

        @NotBlank
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
        String login,

        @NotBlank
        @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
        String password,

        @NotBlank
        @Size(min = 3, max = 50, message = "Account name must be between 3 and 50 characters")
        String accountName,

        @NotBlank
        @Size(min = 3, max = 50, message = "Check Password must be between 3 and 50 characters")
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
