package com.financial_tracker.account_management.dto;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name
) {
}
