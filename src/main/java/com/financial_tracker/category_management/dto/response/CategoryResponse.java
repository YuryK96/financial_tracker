package com.financial_tracker.category_management.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
