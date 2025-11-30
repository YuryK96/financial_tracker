package com.financial_tracker.transaction_processing.dto.request;

import com.financial_tracker.shared.dto.PageRequest;
import org.springframework.data.domain.Pageable;

public record TransactionQuery(
        TransactionFilter filters,
        PageRequest pagination
) {
    public TransactionQuery {
        pagination = pagination != null ? pagination : new PageRequest(null, null, null, null);

        filters = filters != null ? filters : new TransactionFilter(null, null, null, null, null, null);
    }

    public Pageable getPageable() {
        return pagination.getPageable();
    }
}