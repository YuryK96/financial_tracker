package com.financial_tracker.core.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {


}
