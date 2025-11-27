package com.financial_tracker.service;

import com.financial_tracker.account_management.AccountService;
import com.financial_tracker.category_management.dto.Category;
import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.core.transaction.TransactionRepository;
import com.financial_tracker.transaction_processing.*;
import com.financial_tracker.transaction_processing.dto.CategorizedTransaction;
import com.financial_tracker.transaction_processing.dto.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {


    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    private CategorizedTransaction transaction;
    UUID accountId = UUID.randomUUID();

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, accountService);
        Category incomeCategory = new Category("Income");
        SubCategory salarySubCategory = new SubCategory("Salary", incomeCategory);
        transaction = new Income(100.0, Currency.USD, salarySubCategory, accountId);
    }

    @Nested
    @DisplayName("Create transactions")
    class CreateTransactionsTests {

        @Test
        @DisplayName("Should return transaction after adding")
        void shouldReturnTransaction_AfterAdding() {
            when(transactionRepository.getById(transaction.getId())).thenReturn(null);
            when(transactionRepository.save(any(CategorizedTransaction.class))).thenReturn(transaction);

            CategorizedTransaction transaction1 = transactionService.createTransaction(transaction, accountId);

            assertEquals(transaction, transaction1);


        }

        @Test
        @DisplayName("Should not create transaction because duplicate")
        void shouldNotCreateTransaction_BecauseDuplicate() {
            when(transactionRepository.getById(transaction.getId())).thenReturn(transaction);

            assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transaction, accountId));

        }

    }

    @Nested
    @DisplayName("Delete transactions")
    class DeleteTransactionsTests {

        @Test
        @DisplayName("Should return true after adding")
        void shouldReturnTrue_AfterDeleting() {
            when(transactionRepository.getById(transaction.getId())).thenReturn(transaction);
            when(transactionRepository.delete(any(UUID.class))).thenReturn(true);

            boolean isTransactionDeleted = transactionService.deleteTransaction(transaction.getId(), accountId);

            assertTrue(isTransactionDeleted);


        }

        @Test
        @DisplayName("Should not delete transaction because duplicate")
        void shouldNotDeleteTransaction_BecauseDuplicate() {
            when(transactionRepository.getById(transaction.getId())).thenReturn(null);

            assertThrows(IllegalArgumentException.class, () -> transactionService.deleteTransaction(transaction.getId(), accountId));

        }

    }

    @Nested
    @DisplayName("Get transactions by dates")
    class GetTransactionsByDatesTests {

        @Test
        @DisplayName("Should return transactions by dates")
        void shouldReturnTransactions_ByDates() {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = end.minusDays(3);
            List<CategorizedTransaction> transactions = Arrays.asList(transaction);
            when(transactionRepository.getAccountTransactionByCreatedAt(accountId, start, end)).thenReturn(transactions);


            List<CategorizedTransaction> foundTransactions = transactionService.getAccountTransactionByCreatedAt(accountId, start, end);

            assertEquals(1, foundTransactions.size());
            assertEquals(transaction.getId(), foundTransactions.get(0).getId());


        }

        @Test
        @DisplayName("Should return zero transactions by dates")
        void shouldReturnZeroTransactions_ByDates() {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = end.minusDays(3);
            List<CategorizedTransaction> transactions = new ArrayList<>();
            when(transactionRepository.getAccountTransactionByCreatedAt(accountId, start, end)).thenReturn(transactions);


            List<CategorizedTransaction> foundTransactions = transactionService.getAccountTransactionByCreatedAt(accountId, start, end);

            assertEquals(0, foundTransactions.size());

        }

    }


}