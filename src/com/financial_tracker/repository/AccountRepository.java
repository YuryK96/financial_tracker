package com.financial_tracker.repository;

import com.financial_tracker.domain.Account;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface AccountRepository {


    List<Account> getAll();

    Boolean save(Account account);

    Boolean delete(UUID id);

    @Nullable
    Account getById(UUID id);

    @Nullable
    Account getByName(String name);


}
