package com.financial_tracker.core.subcategory;


import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.category.CategoryEntity;
import com.financial_tracker.core.transaction.TransactionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "subcategory")
@Entity
public class SubcategoryEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Formula("(SELECT COUNT(*) FROM transaction t WHERE t.subcategory_id = id)")
    private Long transaction_count;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public SubcategoryEntity() {
    }

    public SubcategoryEntity(String name, UUID id, List<TransactionEntity> transactions, CategoryEntity  category) {
        this.name = name;
        this.transactions = transactions;
        this.category = category;
        this.id = id;

    }



    public SubcategoryEntity(String name, CategoryEntity  category) {
        this.name = name;
        this.transactions = new ArrayList<>();
        this.category = category;

    }

    public Long getTransactionCount() {
        return transaction_count != null ? transaction_count : 0L;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
}
