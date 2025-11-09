package com.financial_tracker.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Account {
    private HashMap<String, Category> categories = new HashMap<>();
    private TreeSet<UUID> transactions = new TreeSet<>();
    private UUID id;
    private String name;

    public Account(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public Account(Category category) {
        this.setCategory(category);
    }


    public Account(Category @NotNull [] categories) {
        for (Category category : categories) {
            this.setCategory(category);
        }
    }

    @Nullable
    public Category getCategory(String categoryName) {
        return this.categories.get(categoryName);
    }

    public void addTransaction(@NotNull Transaction transaction) {
        this.transactions.add(transaction.getId());
    }

    public UUID getId() {
        return id;
    }

    public Category setCategory(@NotNull Category category) throws IllegalArgumentException {
        String categoryName = category.getName();
        if (this.categories.get(categoryName) != null) {
            throw new IllegalArgumentException("Category exist with this name");

        }
      return  this.categories.put(category.getName(), category);
    }

    @Nullable
    public Category removeCategory(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }
      return  this.categories.remove(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return new ArrayList<Category>(categories.values());

    }
    public List<UUID> getTransactionIds() {
        return new ArrayList<UUID>(transactions);

    }

}
