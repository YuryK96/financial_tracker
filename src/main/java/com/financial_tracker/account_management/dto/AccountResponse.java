package com.financial_tracker.account_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
