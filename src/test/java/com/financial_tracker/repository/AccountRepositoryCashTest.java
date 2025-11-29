package com.financial_tracker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryCashTest {
    private AccountRepositoryCash accountRepositoryCash;
    private String VALID_NAME = "Test";

    @BeforeEach
    void setUp() {
        accountRepositoryCash = new AccountRepositoryCash();
    }

    @Nested
    @DisplayName("Get account")
    class GetAccountTests {

        @Test
        @DisplayName("Should get by name")
        void shouldGetByName() {

            Account account = new Account(VALID_NAME);
            accountRepositoryCash.save(account);

            assertEquals(account, accountRepositoryCash.getByName(VALID_NAME));

        }

        @Test
        @DisplayName("Should return different list instance")
        void shouldReturnDifferentListInstance() {
            Account account = new Account(VALID_NAME);
            accountRepositoryCash.save(account);

            List<Account> firstCall = accountRepositoryCash.getAll();
            List<Account> secondCall = accountRepositoryCash.getAll();

            assertNotSame(firstCall, secondCall);
        }

        @Test
        @DisplayName("Should get by by id")
        void shouldGetById() {

            Account account = new Account(VALID_NAME);
            UUID id = account.getId();
            accountRepositoryCash.save(account);

            assertEquals(account, accountRepositoryCash.getById(id));

        }

    }

    @Nested
    @DisplayName("Save account")
    class SaveAccountTests {

        @Test
        @DisplayName("Should save account")
        void shouldSaveAccount_WithValidName() {

            Account account = new Account(VALID_NAME);
            accountRepositoryCash.save(account);
            List<Account> accounts = accountRepositoryCash.getAll();

            assertEquals(1, accounts.size());

        }

        @Test
        @DisplayName("Shouldn't save account with null")
        void shouldGetById() {
            assertThrowsExactly(IllegalArgumentException.class, () -> {
                accountRepositoryCash.save(null);
            });

        }

        @Test
        @DisplayName("Should return null when getting non-existent account by name")
        void shouldReturnNull_WhenAccountNotFoundByName() {
            Account result = accountRepositoryCash.getByName("NonExistent");
            assertNull(result);
        }

        @Test
        @DisplayName("Shouldn't mutate accounts in class")
        void shouldNotMutateAccounts() {

            Account account = new Account(VALID_NAME);
            accountRepositoryCash.save(account);
            List<Account> accounts = accountRepositoryCash.getAll();

            accounts.clear();
            assertEquals(1, accountRepositoryCash.getAll().size());
        }

    }

    @Nested
    @DisplayName("Delete account")
    class DeleteAccountTests {

        @Test
        @DisplayName("Should delete account")
        void shouldDeleteAccount_WithValidName() {

            Account account = new Account(VALID_NAME);
            accountRepositoryCash.delete(account.getId());
            List<Account> accounts = accountRepositoryCash.getAll();

            assertEquals(0, accounts.size());

        }

        @Test
        @DisplayName("Shouldn't delete account with null")
        void shouldNotSaveAccount_WithNull() {
            assertThrowsExactly(IllegalArgumentException.class, () -> {
                accountRepositoryCash.delete(null);
            });

        }



    }

}