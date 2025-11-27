package com.financial_tracker.account_management;

import com.financial_tracker.account_management.dto.Account;
import com.financial_tracker.category_management.dto.Category;
import com.financial_tracker.category_management.SubCategory;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.transaction_processing.dto.CategorizedTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class AccountService {
    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return this.accountRepository.getAll();
    }

    @Nullable
    public Account getAccountByName(String name) {
        return this.accountRepository.getByName(name);
    }


    public boolean createAccount(String name) throws IllegalArgumentException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }

        Account account = new Account(name);
        return this.accountRepository.save(account);
    }


    public boolean deleteAccount(String name) throws IllegalArgumentException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }

        Account account = this.accountRepository.getByName(name);

        if(account == null){
            throw new IllegalArgumentException("Account not found");
        }

        return this.accountRepository.delete(account.getId());
    }


    public Category addCategoryToAccount(String name, UUID accountId) throws IllegalArgumentException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }

        Account account = accountRepository.getById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account didnt found");
        }

        Category category = new Category(name);

        return account.setCategory(category);
    }

    public Category removeCategoryFromAccount(String name, UUID accountId) throws IllegalArgumentException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }

        Account account = accountRepository.getById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account didnt found");
        }


        return account.removeCategory(name);
    }


    public SubCategory addSubCategoryToAccount(String name, UUID accountId, String categoryName) throws IllegalArgumentException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("category name cant be empty");
        }

        Account account = accountRepository.getById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account didnt found");
        }

        Category category = account.getCategory(categoryName);

        if (category == null) {
            throw new IllegalArgumentException("Category didnt found");
        }

        SubCategory subCategory = new SubCategory(name, category);
        return category.addSubCategory(subCategory);

    }

    public boolean addTransaction(@NotNull CategorizedTransaction transaction, UUID accountId) throws IllegalArgumentException {
        Account account = accountRepository.getById(accountId);

        if(account == null){
            throw new IllegalArgumentException("Account not found");
        }

        if(!transaction.getAccountId().equals(accountId)){
            throw new IllegalArgumentException("Transaction has other account id");
        }

        return  account.addTransaction(transaction.getId());
    }

    public boolean removeTransaction(@NotNull CategorizedTransaction transaction, UUID accountId) throws IllegalArgumentException {
        Account account = accountRepository.getById(accountId);

        if(account == null){
            throw new IllegalArgumentException("Account not found");
        }

        if(!transaction.getAccountId().equals(accountId)){
            throw new IllegalArgumentException("Transaction has other account id");
        }

        return  account.removeTransaction(transaction.getId());
    }


    public SubCategory removeSubCategoryFromAccount(String name, UUID accountId, String categoryName) throws IllegalArgumentException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("category name cant be empty");
        }

        Account account = accountRepository.getById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account didnt found");
        }

        Category category = account.getCategory(categoryName);

        if (category == null) {
            throw new IllegalArgumentException("Category didnt found");
        }

        return category.removeSubCategory(name);

    }


}
