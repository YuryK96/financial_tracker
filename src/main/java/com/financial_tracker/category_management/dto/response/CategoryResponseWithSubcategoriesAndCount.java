package com.financial_tracker.category_management.dto.response;

import com.financial_tracker.category_management.dto.SubcategoryResponseWithCount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CategoryResponseWithSubcategoriesAndCount(
        UUID id,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        List<SubcategoryResponseWithCount> subcategories
) {
}
