package com.financial_tracker.account_management;

import com.financial_tracker.account_management.dto.Account;
import com.financial_tracker.core.account.AccountRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountRepositoryCash implements AccountRepository {

    List<Account> accounts = new ArrayList<>();

    @Override
    public List<Account> getAll() {
        return  new ArrayList<Account>(accounts);
    }

    @Override
    public Boolean save(@NotNull Account account) throws IllegalArgumentException {
        Account foundAccountByName = getByName(account.getName());

        if (foundAccountByName != null) {
            throw new IllegalArgumentException("Account with this name already exist");
        }

        return accounts.add(account);
    }

    @Override
    public Boolean delete(UUID id) {

        if(id == null){
            throw new IllegalArgumentException("Id is null");
        }

        int index = getIndexById(id);
        if (index == -1) {
            return false;
        }
        accounts.remove(index);
        return true;
    }

    @Override
    public @Nullable Account getByName(String name) {

        for (Account acc : accounts) {
            if (acc.getName().equals(name)) {
                return acc;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Account getById(UUID id) {
        int index = getIndexById(id);
        if (index == -1) {
            return null;
        }
        return this.accounts.get(index);
    }

    private int getIndexById(UUID id) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(id)) {
                return i;
            }

        }
        return -1;
    }
}
