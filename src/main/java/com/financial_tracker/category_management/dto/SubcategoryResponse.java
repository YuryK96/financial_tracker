package com.financial_tracker.category_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubcategoryResponse(
        UUID id,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}


