package com.financial_tracker.core.account;

import com.financial_tracker.account_management.dto.AccountResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {


    AccountEntity findByName(String name);
}
