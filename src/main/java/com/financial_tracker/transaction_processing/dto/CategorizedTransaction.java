package com.financial_tracker.transaction_processing.dto;

import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;

import java.util.UUID;

public abstract class CategorizedTransaction extends Transaction {
    private SubCategory subCategory;


    public CategorizedTransaction(double amount, Currency currency, SubCategory subCategory, UUID accountId, TransactionType type) {
        super(amount,currency, accountId, type);
        this.subCategory = subCategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
