package com.financial_tracker.domain;

import java.util.UUID;

public class Income extends CategorizedTransaction {

    public Income(double amount,Currency currency, SubCategory subCategory, UUID accountId) {
        super(amount,currency, subCategory, accountId, TransactionType.INCOME);

    }
}
