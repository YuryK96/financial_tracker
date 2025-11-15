package com.financial_tracker.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Account {
    private HashMap<String, Category> categories = new HashMap<>();
    private TreeSet<UUID> transactions = new TreeSet<>();
    private UUID id;
    private String name;

    public Account(String name) throws IllegalArgumentException {

        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Invalid name");
        }

        this.id = UUID.randomUUID();
        this.name = name;
    }

    @Nullable
    public Category getCategory(String categoryName) {
        return this.categories.get(categoryName);
    }

    public boolean addTransaction(UUID transactionId) {
        return this.transactions.add(transactionId);
    }

    public boolean removeTransaction(UUID transactionId) {
        return this.transactions.remove(transactionId);
    }

    public UUID getId() {
        return id;
    }

    public Category setCategory(@NotNull Category category) throws IllegalArgumentException {
        String categoryName = category.getName();
        if (this.categories.get(categoryName) != null) {
            throw new IllegalArgumentException("Category exist with this name");

        }

        this.categories.put(category.getName(), category);
        return category;
    }

    @Nullable
    public Category removeCategory(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }
        return this.categories.remove(name);
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
