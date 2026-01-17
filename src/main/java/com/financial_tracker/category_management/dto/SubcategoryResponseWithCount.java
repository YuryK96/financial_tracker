package com.financial_tracker.category_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubcategoryResponseWithCount(
        UUID id,
        String name,
        Long transaction_count,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
