package com.financial_tracker.core.source;

import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.transaction.TransactionEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Table(name = "source")
@Entity
public class SourceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(name= "name", nullable = false)
    String name;

    @OneToMany(mappedBy = "source",cascade = CascadeType.PERSIST)
    private List<TransactionEntity> transactions;

    public SourceEntity() {}

    public SourceEntity(UUID id,  AccountEntity account, List<TransactionEntity> transactions, String name) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.transactions = transactions;
    }

    public AccountEntity getAccount() {
        return account;
    }
    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public UUID getId() {
        return id;
    }
}
