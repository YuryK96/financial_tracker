package com.financial_tracker.repository;

import com.financial_tracker.category_management.dto.Category;
import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionType;
import com.financial_tracker.transaction_processing.*;
import com.financial_tracker.transaction_processing.dto.CategorizedTransaction;
import com.financial_tracker.transaction_processing.dto.Expense;
import com.financial_tracker.transaction_processing.dto.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryCashTest {
    private TransactionRepositoryCash repository;
    private CategorizedTransaction expenseTransaction;
    private CategorizedTransaction incomeTransaction;
    private UUID accountId;
    private Category foodCategory;
    private SubCategory fruitsSubCategory;

    @BeforeEach
    void setUp() {
        repository = new TransactionRepositoryCash();
        accountId = UUID.randomUUID();

        foodCategory = new Category("Food");
        fruitsSubCategory = new SubCategory("Fruits", foodCategory);

        expenseTransaction = new Expense(100.0, Currency.USD, fruitsSubCategory, accountId);
        incomeTransaction = new Income(500.0, Currency.EU, fruitsSubCategory, accountId);
    }

    @Nested
    @DisplayName("Save transaction tests")
    class SaveTransactionTests {

        @Test
        @DisplayName("✅ Should save transaction and return it")
        void shouldSaveTransaction_AndReturnIt() {
            CategorizedTransaction result = repository.save(expenseTransaction);

            assertEquals(expenseTransaction, result);
            assertEquals(1, repository.findByAccountId(accountId).size());
            assertTrue(repository.findByAccountId(accountId).contains(expenseTransaction));
        }

        @Test
        @DisplayName("✅ Should save multiple transactions")
        void shouldSaveMultipleTransactions() {
            repository.save(expenseTransaction);
            repository.save(incomeTransaction);

            assertEquals(2, repository.findByAccountId(accountId).size());
            assertTrue(repository.findByAccountId(accountId).contains(expenseTransaction));
            assertTrue(repository.findByAccountId(accountId).contains(incomeTransaction));
        }
    }

    @Nested
    @DisplayName("Find by account ID tests")
    class FindByAccountIdTests {

        @Test
        @DisplayName("✅ Should find transactions by account ID")
        void shouldFindTransactions_ByAccountId() {
            UUID anotherAccountId = UUID.randomUUID();
            CategorizedTransaction anotherTransaction = new Expense(50.0, Currency.USD, fruitsSubCategory, anotherAccountId);

            repository.save(expenseTransaction);
            repository.save(incomeTransaction);
            repository.save(anotherTransaction);

            List<CategorizedTransaction> result = repository.findByAccountId(accountId);

            assertEquals(2, result.size());
            assertTrue(result.contains(expenseTransaction));
            assertTrue(result.contains(incomeTransaction));
            assertFalse(result.contains(anotherTransaction));
        }

        @Test
        @DisplayName("✅ Should return empty list for non-existent account ID")
        void shouldReturnEmptyList_ForNonExistentAccountId() {
            List<CategorizedTransaction> result = repository.findByAccountId(UUID.randomUUID());

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by date range tests")
    class FindByRangeTests {

        @Test
        @DisplayName("✅ Should find transactions in date range")
        void shouldFindTransactions_InDateRange() {
            repository.save(expenseTransaction);

            LocalDate today = LocalDate.now();
            LocalDate start = today.minusDays(1);
            LocalDate end = today.plusDays(1);

            List<CategorizedTransaction> result = repository.findByRange(start, end);

            assertEquals(1, result.size());
            assertEquals(expenseTransaction, result.get(0));
        }

        @Test
        @DisplayName("✅ Should return empty list for transactions outside date range")
        void shouldReturnEmptyList_ForTransactionsOutsideDateRange() {
            repository.save(expenseTransaction);

            LocalDate future = LocalDate.now().plusDays(10);
            LocalDate start = future.minusDays(1);
            LocalDate end = future.plusDays(1);

            List<CategorizedTransaction> result = repository.findByRange(start, end);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by type tests")
    class FindByTypeTests {

        @Test
        @DisplayName("✅ Should find transactions by type")
        void shouldFindTransactions_ByType() {
            repository.save(expenseTransaction);
            repository.save(incomeTransaction);

            List<CategorizedTransaction> expenses = repository.findByType(TransactionType.EXPENSE);
            List<CategorizedTransaction> incomes = repository.findByType(TransactionType.INCOME);

            assertEquals(1, expenses.size());
            assertEquals(expenseTransaction, expenses.get(0));

            assertEquals(1, incomes.size());
            assertEquals(incomeTransaction, incomes.get(0));
        }

        @Test
        @DisplayName("✅ Should return empty list for non-existent type")
        void shouldReturnEmptyList_ForNonExistentType() {
            repository.save(expenseTransaction);

            List<CategorizedTransaction> result = repository.findByType(TransactionType.INCOME);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by category tests")
    class FindByCategoryTests {

        @Test
        @DisplayName("✅ Should find transactions by category name")
        void shouldFindTransactions_ByCategoryName() {
            Category transportCategory = new Category("Transport");
            SubCategory fuelSubCategory = new SubCategory("Fuel", transportCategory);
            CategorizedTransaction transportExpense = new Expense(50.0, Currency.USD, fuelSubCategory, accountId);

            repository.save(expenseTransaction);
            repository.save(transportExpense);

            List<CategorizedTransaction> foodTransactions = repository.findByCategory("Food");
            List<CategorizedTransaction> transportTransactions = repository.findByCategory("Transport");

            assertEquals(1, foodTransactions.size());
            assertEquals(expenseTransaction, foodTransactions.get(0));

            assertEquals(1, transportTransactions.size());
            assertEquals(transportExpense, transportTransactions.get(0));
        }

        @Test
        @DisplayName("✅ Should return empty list for non-existent category")
        void shouldReturnEmptyList_ForNonExistentCategory() {
            repository.save(expenseTransaction);

            List<CategorizedTransaction> result = repository.findByCategory("Unknown");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by subcategory tests")
    class FindBySubCategoryTests {

        @Test
        @DisplayName("✅ Should find transactions by subcategory name")
        void shouldFindTransactions_BySubCategoryName() {
            SubCategory vegetablesSubCategory = new SubCategory("Vegetables", foodCategory);
            CategorizedTransaction vegetableExpense = new Expense(30.0, Currency.USD, vegetablesSubCategory, accountId);

            repository.save(expenseTransaction);
            repository.save(vegetableExpense);

            List<CategorizedTransaction> fruitsTransactions = repository.findBySubCategory("Fruits");
            List<CategorizedTransaction> vegetablesTransactions = repository.findBySubCategory("Vegetables");

            assertEquals(1, fruitsTransactions.size());
            assertEquals(expenseTransaction, fruitsTransactions.get(0));

            assertEquals(1, vegetablesTransactions.size());
            assertEquals(vegetableExpense, vegetablesTransactions.get(0));
        }
    }

    @Nested
    @DisplayName("Delete transaction tests")
    class DeleteTransactionTests {

        @Test
        @DisplayName("✅ Should delete existing transaction")
        void shouldDeleteExistingTransaction() {
            repository.save(expenseTransaction);

            boolean result = repository.delete(expenseTransaction.getId());

            assertTrue(result);
            assertTrue(repository.findByAccountId(accountId).isEmpty());
        }

        @Test
        @DisplayName("✅ Should return false for non-existent transaction ID")
        void shouldReturnFalse_ForNonExistentTransactionId() {
            boolean result = repository.delete(UUID.randomUUID());

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Get by ID tests")
    class GetByIdTests {

        @Test
        @DisplayName("✅ Should get transaction by ID")
        void shouldGetTransaction_ById() {
            repository.save(expenseTransaction);

            CategorizedTransaction result = repository.getById(expenseTransaction.getId());

            assertEquals(expenseTransaction, result);
        }

        @Test
        @DisplayName("✅ Should return null for non-existent ID")
        void shouldReturnNull_ForNonExistentId() {
            CategorizedTransaction result = repository.getById(UUID.randomUUID());

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Get account transactions by createdAt tests")
    class GetAccountTransactionByCreatedAtTests {

        @Test
        @DisplayName("✅ Should find transactions by account ID and date time range")
        void shouldFindTransactions_ByAccountIdAndDateTimeRange() {
            repository.save(expenseTransaction);

            LocalDateTime start = LocalDateTime.now().minusHours(1);
            LocalDateTime end = LocalDateTime.now().plusHours(1);

            List<CategorizedTransaction> result = repository.getAccountTransactionByCreatedAt(accountId, start, end);

            assertEquals(1, result.size());
            assertEquals(expenseTransaction, result.get(0));
        }

        @Test
        @DisplayName("✅ Should return empty list for wrong account ID")
        void shouldReturnEmptyList_ForWrongAccountId() {
            repository.save(expenseTransaction);

            LocalDateTime start = LocalDateTime.now().minusHours(1);
            LocalDateTime end = LocalDateTime.now().plusHours(1);

            List<CategorizedTransaction> result = repository.getAccountTransactionByCreatedAt(UUID.randomUUID(), start, end);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Collection copy protection tests")
    class CollectionCopyProtectionTests {

        @Test
        @DisplayName("🛡️ Should return copy from findByAccountId - protecting internal state")
        void shouldReturnCopy_FromFindByAccountId() {
            repository.save(expenseTransaction);
            repository.save(incomeTransaction);

            List<CategorizedTransaction> result = repository.findByAccountId(accountId);


            result.clear();
            result.add(expenseTransaction);


            assertEquals(2, repository.findByAccountId(accountId).size());
            assertTrue(repository.findByAccountId(accountId).contains(expenseTransaction));
            assertTrue(repository.findByAccountId(accountId).contains(incomeTransaction));
        }

        @Test
        @DisplayName("🛡️ Should return copy from findByRange - protecting internal state")
        void shouldReturnCopy_FromFindByRange() {
            repository.save(expenseTransaction);

            LocalDate today = LocalDate.now();
            List<CategorizedTransaction> result = repository.findByRange(today.minusDays(1), today.plusDays(1));


            result.clear();


            assertEquals(1, repository.findByRange(today.minusDays(1), today.plusDays(1)).size());
        }

        @Test
        @DisplayName("🛡️ Should return copy from findByType - protecting internal state")
        void shouldReturnCopy_FromFindByType() {
            repository.save(expenseTransaction);

            List<CategorizedTransaction> result = repository.findByType(TransactionType.EXPENSE);

            result.add(incomeTransaction);


            assertEquals(1, repository.findByType(TransactionType.EXPENSE).size());
            assertEquals(expenseTransaction, repository.findByType(TransactionType.EXPENSE).get(0));
        }

        @Test
        @DisplayName("🛡️ Should return copy from findByCategory - protecting internal state")
        void shouldReturnCopy_FromFindByCategory() {
            repository.save(expenseTransaction);

            List<CategorizedTransaction> result = repository.findByCategory("Food");


            Category newCategory = new Category("New Category");
            SubCategory newSubCategory = new SubCategory("New Sub", newCategory);
            CategorizedTransaction newTransaction = new Expense(200.0, Currency.USD, newSubCategory, accountId);
            result.add(newTransaction);


            assertEquals(1, repository.findByCategory("Food").size());
            assertEquals(expenseTransaction, repository.findByCategory("Food").get(0));
        }

        @Test
        @DisplayName("🛡️ Should return copy from findBySubCategory - protecting internal state")
        void shouldReturnCopy_FromFindBySubCategory() {
            repository.save(expenseTransaction);

            List<CategorizedTransaction> result = repository.findBySubCategory("Fruits");


            result.remove(0);


            assertEquals(1, repository.findBySubCategory("Fruits").size());
            assertEquals(expenseTransaction, repository.findBySubCategory("Fruits").get(0));
        }

        @Test
        @DisplayName("🛡️ Should return copy from getAccountTransactionByCreatedAt - protecting internal state")
        void shouldReturnCopy_FromGetAccountTransactionByCreatedAt() {
            repository.save(expenseTransaction);

            LocalDateTime start = LocalDateTime.now().minusHours(1);
            LocalDateTime end = LocalDateTime.now().plusHours(1);
            List<CategorizedTransaction> result = repository.getAccountTransactionByCreatedAt(accountId, start, end);


            result.clear();


            assertEquals(1, repository.getAccountTransactionByCreatedAt(accountId, start, end).size());
            assertEquals(expenseTransaction, repository.getAccountTransactionByCreatedAt(accountId, start, end).get(0));
        }

        @Test
        @DisplayName("🛡️ Multiple calls should return different copies")
        void multipleCalls_ShouldReturnDifferentCopies() {
            repository.save(expenseTransaction);

            List<CategorizedTransaction> copy1 = repository.findByAccountId(accountId);
            List<CategorizedTransaction> copy2 = repository.findByAccountId(accountId);


            assertNotSame(copy1, copy2);

            copy1.clear();
            assertEquals(0, copy1.size());
            assertEquals(1, copy2.size());
            assertEquals(1, repository.findByAccountId(accountId).size());
        }
    }


}