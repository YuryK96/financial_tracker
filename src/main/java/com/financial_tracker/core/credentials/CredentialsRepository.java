package com.financial_tracker.core.credentials;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, UUID> {


    Optional<CredentialsEntity> findByLogin(String login);

    boolean existsByLogin(String login);
}
