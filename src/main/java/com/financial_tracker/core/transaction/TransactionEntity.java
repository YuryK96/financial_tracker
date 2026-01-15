package com.financial_tracker.core.transaction;

import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.source.SourceEntity;
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


    @Column(name="source_id", insertable = false,updatable = false)
    private UUID sourceId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description", nullable = true)
    private String description;

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
    @JoinColumn(name="source_id", nullable = false)
    private SourceEntity source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryEntity subcategory;

    public TransactionEntity() {
    }

    public TransactionEntity(UUID id, BigDecimal amount, Currency currency, TransactionType type, AccountEntity account, SubcategoryEntity subCategory, String description, SourceEntity source) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.source = source;
        this.account = account;
        this.subcategory = subCategory;
        this.description = description;
    }

    public SubcategoryEntity getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubcategoryEntity subcategory) {
        this.subcategory = subcategory;
    }

    public SourceEntity getSource() {
        return source;
    }

    public void setSource(SourceEntity source) {
        this.source = source;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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
    public UUID getSourceId() {
        return sourceId;
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

    @Override
    public String toString(){
        return "TransactionEntity{" +
                "id=" + id +
                ", amount=" + amount +
                ", currency=" + currency +
                ", type=" + type +
                ", description=" + description +
                ", account=" + (account != null ? account.getId() : "null") +
                ", subcategory=" + (subcategory != null ? subcategory.getId() : "null") +
                ", source=" + (source != null ? source.getId() : "null") +
                ", sourceId=" + sourceId +
                '}';

    }
}
