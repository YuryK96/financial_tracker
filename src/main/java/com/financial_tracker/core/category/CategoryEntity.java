package com.financial_tracker.core.category;

import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.subcategory.SubCategoryEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Table(name = "category",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "name", "account_id"
        }))
@Entity
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;


    @Column(name = "name", nullable = false, length = 50)
    private String name;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubCategoryEntity> subCategories;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;


    public CategoryEntity() {
    }

    public CategoryEntity(String name, AccountEntity account,  List<SubCategoryEntity> subCategories, UUID id) {
        this.name = name;
        this.id = id;
        this.subCategories = subCategories;
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return account != null ? account.getId() : null;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategoryEntity> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryEntity> subCategories) {
        this.subCategories = subCategories;
    }
}
