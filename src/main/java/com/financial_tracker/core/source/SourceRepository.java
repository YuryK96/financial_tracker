package com.financial_tracker.core.source;

import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.credentials.CredentialsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceRepository extends JpaRepository<SourceEntity, UUID> {


    boolean existsByIdAndAccount(UUID id, AccountEntity account);

    boolean existsByIdAndAccountId(UUID id, UUID accountId);

    boolean existsByNameAndAccount_Id(String name, UUID accountId);

   Optional<SourceEntity>  findByIdAndAccount_Id(UUID id, UUID accountId);

    Page<SourceEntity> findAllByAccount_Id(UUID accountId, Pageable pageable);
}
