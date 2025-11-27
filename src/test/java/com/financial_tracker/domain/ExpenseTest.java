package com.financial_tracker.domain;

import com.financial_tracker.category_management.dto.Category;
import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.transaction_processing.dto.CategorizedTransaction;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.transaction_processing.dto.Expense;
import com.financial_tracker.core.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    private SubCategory fruitsSubCategory;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        Category foodCategory = new Category("Food");
        fruitsSubCategory = new SubCategory("Fruits", foodCategory);
        accountId = UUID.randomUUID();
    }

    @Test
    @DisplayName("✅ Should create expense with correct properties")
    void shouldCreateExpense_WithCorrectProperties() {
        Expense expense = new Expense(100.0, Currency.USD, fruitsSubCategory, accountId);

        assertEquals(100.0, expense.getAmount());
        assertEquals(Currency.USD, expense.getCurrency());
        assertEquals(fruitsSubCategory, expense.getSubCategory());
        assertEquals(accountId, expense.getAccountId());
        assertEquals(TransactionType.EXPENSE, expense.getType());
    }

    @Test
    @DisplayName("✅ Should be instance of CategorizedTransaction")
    void shouldBeInstanceOf_CategorizedTransaction() {
        Expense expense = new Expense(75.0, Currency.BYN, fruitsSubCategory, accountId);

        assertTrue(expense instanceof CategorizedTransaction);
        assertEquals(TransactionType.EXPENSE, expense.getType());
    }
}