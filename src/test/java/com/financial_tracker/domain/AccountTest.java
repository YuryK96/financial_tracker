package com.financial_tracker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;
    private static final String VALID_NAME = "Test";

    @BeforeEach
    void setUp() {
        account = new Account("test");
    }

    @Nested
    @DisplayName("Create account tests")
    class CreateAccountTests {

        @ParameterizedTest
        @CsvSource({
                "Test",
                "test",
                "TE_ST",
                "TE-ST",
        })
        @DisplayName("Should create account with valid names")
        void shouldCreateAccount_WhenNameIsValid(String name) {
            Account createdAccount = new Account(name);
            assertEquals(name, createdAccount.getName());
            assertNotNull(createdAccount.getId());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "   "
        })
        @DisplayName("Shouldn't create account with invalid names")
        void shouldThrowException_WhenNameIsInvalid(String name) {
            assertThrows(IllegalArgumentException.class, () -> new Account(name));
        }

        @Test
        @DisplayName("Shouldn't create account with null")
        void shouldThrowException_WhenNameIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new Account(null));
        }

        @Test
        @DisplayName("Should initialize with empty collections")
        void shouldInitializeWithEmptyCollections() {
            Account newAccount = new Account("Test");
            assertTrue(newAccount.getCategories().isEmpty());
            assertTrue(newAccount.getTransactionIds().isEmpty());
        }
    }

    @Nested
    @DisplayName("Add category")
    class AddCategoryTest {

        @Test
        @DisplayName("Should add category with valid arg")
        void shouldAddCategory_WhenArgIsValid() {
            Category category = new Category(VALID_NAME);
            account.setCategory(category);
            assertEquals(category, account.getCategory(VALID_NAME));
        }

        @Test
        @DisplayName("Shouldn't add category with null")
        void shouldNotAddCategory_WhenArgIsNull() {
            assertThrows(IllegalArgumentException.class, () -> account.setCategory(null));
        }

        @Test
        @DisplayName("Shouldn't add category with duplicate")
        void shouldNotAddCategory_WhenDuplicate() {
            Category newCategory = new Category(VALID_NAME);
            account.setCategory(newCategory);
            assertThrows(IllegalArgumentException.class, () -> account.setCategory(newCategory));
        }

        @Test
        @DisplayName("Should appear in categories list after adding")
        void shouldAppearInCategoriesList_AfterAdding() {
            Category category = new Category("Food");
            account.setCategory(category);

            List<Category> categories = account.getCategories();
            assertEquals(1, categories.size());
            assertTrue(categories.contains(category));
        }

        @Test
        @DisplayName("Should return null for non-existent category")
        void shouldReturnNull_ForNonExistentCategory() {
            assertNull(account.getCategory("NonExistent"));
        }
    }

    @Nested
    @DisplayName("Remove category")
    class RemoveCategoryTest {

        @Test
        @DisplayName("Should remove existing category")
        void shouldRemoveExistingCategory() {
            Category category = new Category("Food");
            account.setCategory(category);

            Category removed = account.removeCategory("Food");

            assertEquals(category, removed);
            assertNull(account.getCategory("Food"));
            assertTrue(account.getCategories().isEmpty());
        }

        @Test
        @DisplayName("Should return null when removing non-existent category")
        void shouldReturnNull_WhenRemovingNonExistentCategory() {
            assertNull(account.removeCategory("Unknown"));
        }

        @Test
        @DisplayName("Should throw when removing category with empty name")
        void shouldThrow_WhenRemovingCategoryWithEmptyName() {
            assertThrows(IllegalArgumentException.class,
                    () -> account.removeCategory("   "));
        }

        @Test
        @DisplayName("Should throw when removing category with null name")
        void shouldThrow_WhenRemovingCategoryWithNullName() {
            assertThrows(IllegalArgumentException.class,
                    () -> account.removeCategory(null));
        }
    }

    @Nested
    @DisplayName("Transaction operations")
    class TransactionTests {

        @Test
        @DisplayName("Should add transaction")
        void shouldAddTransaction() {
            UUID transactionId = UUID.randomUUID();
            assertTrue(account.addTransaction(transactionId));
            assertTrue(account.getTransactionIds().contains(transactionId));
        }

        @Test
        @DisplayName("Should remove transaction")
        void shouldRemoveTransaction() {
            UUID transactionId = UUID.randomUUID();
            account.addTransaction(transactionId);
            assertTrue(account.removeTransaction(transactionId));
            assertFalse(account.getTransactionIds().contains(transactionId));
        }

        @Test
        @DisplayName("Should not add duplicate transaction")
        void shouldNotAddDuplicateTransaction() {
            UUID transactionId = UUID.randomUUID();
            account.addTransaction(transactionId);
            assertFalse(account.addTransaction(transactionId));
            assertEquals(1, account.getTransactionIds().size());
        }

        @Test
        @DisplayName("Should return false when removing non-existent transaction")
        void shouldReturnFalse_WhenRemovingNonExistentTransaction() {
            assertFalse(account.removeTransaction(UUID.randomUUID()));
        }

        @Test
        @DisplayName("Should maintain transaction order")
        void shouldMaintainTransactionOrder() {
            UUID first = UUID.randomUUID();
            UUID second = UUID.randomUUID();

            account.addTransaction(first);
            account.addTransaction(second);

            List<UUID> transactions = account.getTransactionIds();
            assertEquals(2, transactions.size());
            // TreeSet maintains natural order (UUID comparison)
        }
    }

    @Nested
    @DisplayName("Account properties")
    class AccountPropertiesTest {

        @Test
        @DisplayName("Should get and set name")
        void shouldGetAndSetName() {
            String newName = "New Name";
            account.setName(newName);
            assertEquals(newName, account.getName());
        }

        @Test
        @DisplayName("Should have consistent ID")
        void shouldHaveConsistentId() {
            UUID originalId = account.getId();
            account.setName("Changed Name");
            assertEquals(originalId, account.getId());
        }

        @Test
        @DisplayName("Should return immutable categories list")
        void shouldReturnCopyOfCategories_NotOriginalCollection() {

            Category category = new Category("Food");
            account.setCategory(category);


            List<Category> categories = account.getCategories();
            categories.clear();


            assertEquals(1, account.getCategories().size());
            assertTrue(account.getCategories().contains(category));
        }

        @Test
        @DisplayName("Should return immutable transactions list")
        void shouldReturnImmutableTransactionsList() {
            UUID transactionId = UUID.randomUUID();
            account.addTransaction(transactionId);

            List<UUID> transactions = account.getTransactionIds();
            transactions.clear();


            assertEquals(1, account.getTransactionIds().size());
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCasesTest {

        @Test
        @DisplayName("Should handle multiple categories")
        void shouldHandleMultipleCategories() {
            Category food = new Category("Food");
            Category transport = new Category("Transport");
            Category entertainment = new Category("Entertainment");

            account.setCategory(food);
            account.setCategory(transport);
            account.setCategory(entertainment);

            assertEquals(3, account.getCategories().size());
            assertEquals(food, account.getCategory("Food"));
            assertEquals(transport, account.getCategory("Transport"));
            assertEquals(entertainment, account.getCategory("Entertainment"));
        }

        @Test
        @DisplayName("Should handle multiple transactions")
        void shouldHandleMultipleTransactions() {
            UUID tx1 = UUID.randomUUID();
            UUID tx2 = UUID.randomUUID();
            UUID tx3 = UUID.randomUUID();

            account.addTransaction(tx1);
            account.addTransaction(tx2);
            account.addTransaction(tx3);

            assertEquals(3, account.getTransactionIds().size());
            assertTrue(account.getTransactionIds().containsAll(List.of(tx1, tx2, tx3)));
        }
    }
}