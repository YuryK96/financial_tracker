package com.financial_tracker.category_management.dto.request;

import com.financial_tracker.shared.dto.PageRequest;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;

public record CategorySearch(
        @Nullable
        String name,
        PageRequest pagination
) {
    public CategorySearch {
        pagination = pagination != null ? pagination : new PageRequest(null, null, null, null);
    }

    public Pageable getPageable() {
        return pagination.getPageable();
    }
}
