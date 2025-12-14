package com.financial_tracker.shared.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String[] errors,

        LocalDateTime at
) {
}
