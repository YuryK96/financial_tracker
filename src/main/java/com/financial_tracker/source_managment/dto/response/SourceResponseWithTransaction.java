package com.financial_tracker.source_managment.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SourceResponseWithTransaction(
        UUID id,
        String name,
       Long transaction_count,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
