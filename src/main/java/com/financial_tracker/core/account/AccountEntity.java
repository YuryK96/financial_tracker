package com.financial_tracker.core.account;


import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.category.CategoryEntity;
import com.financial_tracker.core.credentials.CredentialsEntity;
import com.financial_tracker.core.source.SourceEntity;
import com.financial_tracker.core.transaction.TransactionEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Table(name = "account")
@Entity
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CredentialsEntity credentials;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<CategoryEntity> categories;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<SourceEntity> sources;


    public AccountEntity() {}
    public AccountEntity(String name) {
        this.name = name;
    }

    public AccountEntity(UUID id, String name, List<TransactionEntity> transactions, List<SourceEntity> sources) {
        this.id = id;
        this.name = name;
        this.transactions = transactions;
        this.sources = sources;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public List<SourceEntity> getSources() {
        return sources;
    }

    public void setSources(List<SourceEntity> sources) {
        this.sources = sources;
    }
}
