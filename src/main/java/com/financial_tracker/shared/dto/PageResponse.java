package com.financial_tracker.shared.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int pageSize,
        int currentPage,
        long totalElements,
        int totalPages
) {

    public static <T> PageResponse<T> of(Page<T> page) {

        return new PageResponse(
                page.getContent(),
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );

    }

}
