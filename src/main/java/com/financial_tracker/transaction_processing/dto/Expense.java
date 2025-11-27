package com.financial_tracker.transaction_processing.dto;

import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;

import java.util.UUID;

public class Expense extends CategorizedTransaction {

    public Expense(double amount, Currency currency, SubCategory subCategory, UUID accountId){
        super(amount,currency, subCategory, accountId, TransactionType.EXPENSE);
    }
}
