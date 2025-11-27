package com.financial_tracker.transaction_processing;

import com.financial_tracker.core.transaction.TransactionRepository;
import com.financial_tracker.core.transaction.TransactionType;
import com.financial_tracker.transaction_processing.dto.CategorizedTransaction;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionRepositoryCash implements TransactionRepository {

    private List<CategorizedTransaction>  transactions = new ArrayList<>();


    @Override
    public CategorizedTransaction save(CategorizedTransaction transaction) {
        this.transactions.add(transaction);
        return transaction;
    }

    @Override
    public List<CategorizedTransaction> findByAccountId(UUID accountId) {
        return transactions
                .stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorizedTransaction> findByRange(LocalDate start, LocalDate end) {
        return transactions
                .stream()
                .filter(t -> !t.getCreateAt().isBefore(start.atStartOfDay()) && !t.getCreateAt().isAfter(end.atTime(LocalTime.MAX)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorizedTransaction> findByType(TransactionType type) {
        return transactions
                .stream()
                .filter(t -> t.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorizedTransaction> findByCategory(String categoryName) {
        return transactions
                .stream()
                .filter(t -> t.getSubCategory().getCategory().getName().equals(categoryName))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorizedTransaction> findBySubCategory(String subCategoryName) {
        return transactions
                .stream()
                .filter(t -> t.getSubCategory().getName().equals(subCategoryName))
                .collect(Collectors.toList());
    }


    @Override
    public boolean delete(UUID id) {
        int index = getIndexById(id);
        if (index == -1) {
            return false;
        }
        transactions.remove(index);
        return true;
    }


    @Nullable
    @Override
    public CategorizedTransaction getById(UUID id) {
        int index = getIndexById(id);
        if (index == -1) {
            return null;
        }
        return this.transactions.get(index);
    }

    private int getIndexById(UUID id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(id)) {
                return i;
            }

        }
        return -1;
    }


    public List<CategorizedTransaction> getAccountTransactionByCreatedAt(UUID accountId, LocalDateTime start, LocalDateTime end) {

        return this.transactions
                .stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .filter(t -> t.getCreateAt().isAfter(start) && t.getCreateAt().isBefore(end))
                .collect(Collectors.toList());

    }

}

