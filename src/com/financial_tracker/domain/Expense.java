package com.financial_tracker.domain;

import java.util.UUID;

public class Expense extends CategorizedTransaction {

    public Expense(double amount, Currency currency, SubCategory subCategory, UUID accountId){
        super(amount,currency, subCategory, accountId, TransactionType.EXPENSE);
    }
}
