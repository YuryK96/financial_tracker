package com.financial_tracker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IncomeTest {
    private SubCategory salarySubCategory;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        Category incomeCategory = new Category("Income");
        salarySubCategory = new SubCategory("Salary", incomeCategory);
        accountId = UUID.randomUUID();
    }

    @Test
    @DisplayName("✅ Should create income with correct properties")
    void shouldCreateIncome_WithCorrectProperties() {
        Income income = new Income(1000.0, Currency.USD, salarySubCategory, accountId);

        assertEquals(1000.0, income.getAmount());
        assertEquals(Currency.USD, income.getCurrency());
        assertEquals(salarySubCategory, income.getSubCategory());
        assertEquals(accountId, income.getAccountId());
        assertEquals(TransactionType.INCOME, income.getType());
    }

    @Test
    @DisplayName("✅ Should be instance of CategorizedTransaction")
    void shouldBeInstanceOf_CategorizedTransaction() {
        Income income = new Income(750.0, Currency.BYN, salarySubCategory, accountId);

        assertTrue(income instanceof CategorizedTransaction);
        assertEquals(TransactionType.INCOME, income.getType());
    }
}