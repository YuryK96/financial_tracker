package com.financial_tracker.source_managment.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SourceResponse(
        UUID id,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
