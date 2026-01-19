package com.financial_tracker.core.google_token.account;

import com.financial_tracker.core.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GoogleTokenRepository extends JpaRepository<GoogleTokenEntity, UUID> {
    Optional<GoogleTokenEntity> findByAccountId(UUID accountId);

    Optional<GoogleTokenEntity> findByAccount(AccountEntity account);

    boolean existsByAccountId(UUID accountId);

    void deleteByAccountId(UUID accountId);
}