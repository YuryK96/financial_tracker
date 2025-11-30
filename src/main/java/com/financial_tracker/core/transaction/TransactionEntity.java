package com.financial_tracker.core.transaction;

import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

import java.util.UUID;

@Table(name = "transaction")
@Entity
public class TransactionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryEntity subcategory;

    public TransactionEntity() {
    }

    public TransactionEntity(UUID id, BigDecimal amount, Currency currency, TransactionType type, AccountEntity account, SubcategoryEntity subCategory) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.account = account;
        this.subcategory = subCategory;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public SubcategoryEntity getSubCategory() {
        return subcategory;
    }

    public void setSubCategory(SubcategoryEntity subCategory) {
        this.subcategory = subCategory;
    }
}
