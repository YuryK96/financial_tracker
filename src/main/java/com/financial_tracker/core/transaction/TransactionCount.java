package com.financial_tracker.core.transaction;

import java.util.UUID;

public record TransactionCount(
        UUID itemId,
        Long count
) {}