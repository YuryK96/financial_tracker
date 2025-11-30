package com.financial_tracker.category_management.dto.response;

import com.financial_tracker.category_management.dto.SubcategoryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CategoryResponseWithSubcategories(
        UUID id,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        List<SubcategoryResponse> subcategories
) {
}
