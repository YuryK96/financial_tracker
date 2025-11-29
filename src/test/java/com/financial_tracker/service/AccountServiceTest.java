package com.financial_tracker.service;

import com.financial_tracker.account_management.AccountService;
import com.financial_tracker.category_management.dto.Category;
import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.transaction_processing.dto.CategorizedTransaction;
import com.financial_tracker.core.transaction.Currency;
import com.financial_tracker.transaction_processing.dto.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;
    private Account account;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository);
        accountId = UUID.randomUUID();
        account = new Account("Test Account");
    }

    @Nested
    @DisplayName("Get accounts")
    class GetAccountsTests {

        @Test
        @DisplayName("Should return all accounts")
        void shouldReturnAllAccounts() {
            List<Account> accounts = Arrays.asList(account);
            when(accountRepository.getAll()).thenReturn(accounts);

            List<Account> foundAccounts = accountService.getAllAccounts();

            assertEquals(1, foundAccounts.size());
            assertEquals(account.getName(), foundAccounts.get(0).getName());
        }

        @Test
        @DisplayName("Should return account by name")
        void shouldReturnAccount_ByName() {
            when(accountRepository.getByName("Test Account")).thenReturn(account);

            Account foundAccount = accountService.getAccountByName("Test Account");

            assertNotNull(foundAccount);
            assertEquals(account.getName(), foundAccount.getName());
        }

        @Test
        @DisplayName("Should return null when account not found by name")
        void shouldReturnNull_WhenAccountNotFoundByName() {
            when(accountRepository.getByName("Unknown")).thenReturn(null);

            Account foundAccount = accountService.getAccountByName("Unknown");

            assertNull(foundAccount);
        }
    }

    @Nested
    @DisplayName("Create accounts")
    class CreateAccountsTests {

        @Test
        @DisplayName("Should create account successfully")
        void shouldCreateAccount_Successfully() {
            when(accountRepository.save(any(Account.class))).thenReturn(true);

            boolean result = accountService.createAccount("New Account");

            assertTrue(result);
        }

        @Test
        @DisplayName("Should not create account because empty name")
        void shouldNotCreateAccount_BecauseEmptyName() {
            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(""));
            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount("   "));
            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(null));
        }
    }

    @Nested
    @DisplayName("Delete accounts")
    class DeleteAccountsTests {

        @Test
        @DisplayName("Should delete account successfully")
        void shouldDeleteAccount_Successfully() {
            when(accountRepository.getByName("Test Account")).thenReturn(account);
            when(accountRepository.delete(account.getId())).thenReturn(true);

            boolean result = accountService.deleteAccount("Test Account");

            assertTrue(result);
        }

        @Test
        @DisplayName("Should not delete account because not found")
        void shouldNotDeleteAccount_BecauseNotFound() {
            when(accountRepository.getByName("Unknown")).thenReturn(null);

            assertThrows(IllegalArgumentException.class, () -> accountService.deleteAccount("Unknown"));
        }

        @Test
        @DisplayName("Should not delete account because empty name")
        void shouldNotDeleteAccount_BecauseEmptyName() {
            assertThrows(IllegalArgumentException.class, () -> accountService.deleteAccount(""));
        }
    }

    @Nested
    @DisplayName("Category operations")
    class CategoryOperationsTests {



        @Test
        @DisplayName("Should add category to account")
        void shouldAddCategory_ToAccount() {
            when(accountRepository.getById(accountId)).thenReturn(account);
            Category category = accountService.addCategoryToAccount("New Category", accountId);

            assertNotNull(category);
            assertEquals("New Category", category.getName());
        }

        @Test
        @DisplayName("Should not add category because empty name")
        void shouldNotAddCategory_BecauseEmptyName() {
            assertThrows(IllegalArgumentException.class, () -> accountService.addCategoryToAccount("", accountId));
        }

        @Test
        @DisplayName("Should not add category because account not found")
        void shouldNotAddCategory_BecauseAccountNotFound() {
            when(accountRepository.getById(accountId)).thenReturn(account);
            when(accountRepository.getById(accountId)).thenReturn(null);

            assertThrows(IllegalArgumentException.class, () -> accountService.addCategoryToAccount("Category", accountId));
        }

        @Test
        @DisplayName("Should remove category from account")
        void shouldRemoveCategory_FromAccount() {
            when(accountRepository.getById(accountId)).thenReturn(account);
            Category category = accountService.addCategoryToAccount("Test Category", accountId);

            Category removedCategory = accountService.removeCategoryFromAccount("Test Category", accountId);

            assertNotNull(removedCategory);
            assertEquals("Test Category", removedCategory.getName());
        }

        @Test
        @DisplayName("Should not remove category because not found")
        void shouldNotRemoveCategory_BecauseNotFound() {
            when(accountRepository.getById(accountId)).thenReturn(account);
           Category removedCategory = accountService.removeCategoryFromAccount("Unknown", accountId);

           assertNull(removedCategory);
        }
    }

    @Nested
    @DisplayName("SubCategory operations")
    class SubCategoryOperationsTests {

        private Category category;

        @BeforeEach
        void setUp() {
            category = new Category("Main Category");
            account.setCategory(category);
        }

        @Test
        @DisplayName("Should add subcategory to account")
        void shouldAddSubCategory_ToAccount() {
            when(accountRepository.getById(accountId)).thenReturn(account);
            SubCategory subCategory = accountService.addSubCategoryToAccount("New SubCategory", accountId, "Main Category");

            assertNotNull(subCategory);
            assertEquals("New SubCategory", subCategory.getName());
        }

        @Test
        @DisplayName("Should not add subcategory because category not found")
        void shouldNotAddSubCategory_BecauseCategoryNotFound() {
            when(accountRepository.getById(accountId)).thenReturn(account);
            assertThrows(IllegalArgumentException.class,
                    () -> accountService.addSubCategoryToAccount("SubCategory", accountId, "Unknown Category"));
        }

        @Test
        @DisplayName("Should remove subcategory from account")
        void shouldRemoveSubCategory_FromAccount() {
            when(accountRepository.getById(accountId)).thenReturn(account);
             category.addSubCategory("Test SubCategory");

            SubCategory removedSubCategory = accountService.removeSubCategoryFromAccount("Test SubCategory", accountId, "Main Category");

            assertNotNull(removedSubCategory);
            assertEquals("Test SubCategory", removedSubCategory.getName());
        }

        @Test
        @DisplayName("Should not remove subcategory because empty names")
        void shouldNotRemoveSubCategory_BecauseEmptyNames() {
            assertThrows(IllegalArgumentException.class,
                    () -> accountService.removeSubCategoryFromAccount("", accountId, "Main Category"));
            assertThrows(IllegalArgumentException.class,
                    () -> accountService.removeSubCategoryFromAccount("SubCategory", accountId, ""));
        }
    }

    @Nested
    @DisplayName("Transaction operations")
    class TransactionOperationsTests {

        private CategorizedTransaction transaction;

        @BeforeEach
        void setUp() {
            transaction = new Income(100.0, Currency.USD, new SubCategory("Salary", new Category("Income")), accountId);
            when(accountRepository.getById(accountId)).thenReturn(account);
        }

        @Test
        @DisplayName("Should add transaction to account")
        void shouldAddTransaction_ToAccount() {
            boolean result = accountService.addTransaction(transaction, accountId);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should not add transaction because account not found")
        void shouldNotAddTransaction_BecauseAccountNotFound() {
            when(accountRepository.getById(accountId)).thenReturn(null);

            assertThrows(IllegalArgumentException.class, () -> accountService.addTransaction(transaction, accountId));
        }

        @Test
        @DisplayName("Should not add transaction because account id mismatch")
        void shouldNotAddTransaction_BecauseAccountIdMismatch() {
            UUID differentAccountId = UUID.randomUUID();
            CategorizedTransaction wrongTransaction = new Income(100.0, Currency.USD, new SubCategory("Salary", new Category("Income")), differentAccountId);

            assertThrows(IllegalArgumentException.class, () -> accountService.addTransaction(wrongTransaction, accountId));
        }

        @Test
        @DisplayName("Should remove transaction from account")
        void shouldRemoveTransaction_FromAccount() {
            account.addTransaction(transaction.getId());

            boolean result = accountService.removeTransaction(transaction, accountId);

            assertTrue(result);
        }
    }
}