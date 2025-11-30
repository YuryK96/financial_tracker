package com.financial_tracker.shared.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageRequest(
        @Min(value = 0, message = "Page must be greater than or equal to 0")
        Integer page,
        @Min(value = 1, message = "Page size must be greater than or equal to 1")
        @Max(value = 100, message = "Page size must be less than or equal to 100")
        Integer size,
        String sortBy,
        Sort.Direction sortDirection
) {

    public PageRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 20;
        if (sortBy == null) sortBy = "createdAt";
        if (sortDirection == null) sortDirection = Sort.Direction.DESC;
    }

    public Pageable getPageable() {
        return org.springframework.data.domain.PageRequest.of(page, size, sortDirection, sortBy);
    }
}
