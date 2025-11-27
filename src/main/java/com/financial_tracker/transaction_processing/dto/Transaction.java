package com.financial_tracker.transaction_processing.dto;

import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transaction {
    private UUID id;
    private final UUID accountId;
    private double amount;
    private Currency currency;
    private final LocalDateTime createAt;
    private final TransactionType type;


    public Transaction(double amount, Currency currency, UUID accountId, TransactionType type) {
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.accountId = accountId;
        this.id = UUID.randomUUID();
        this.createAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Transaction_" + this.id + ": " + this.amount + " " + this.currency;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public UUID getId() {
        return id;
    }


}
