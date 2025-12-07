package com.financial_tracker.account_management;

import com.financial_tracker.account_management.dto.AccountResponse;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public PageResponse<AccountResponse> getAllAccounts(PageRequest pageRequest) {
        log.debug("Getting all accounts - page: {}, size: {}", pageRequest.page(), pageRequest.size());

        Pageable pageable = pageRequest.getPageable();
        Page<AccountEntity> page = this.accountRepository.findAll(pageable);

        return PageResponse.of(page.map(accountMapper::toResponse));
    }

    @Nullable
    public AccountResponse getAccountByName(String name) {
        log.debug("Getting account by name: {}", name);

        AccountEntity foundAccount = this.accountRepository.findByName(name);

        if (foundAccount == null) {
            throw new EntityNotFoundException("Account not found");
        }

        return accountMapper.toResponse(foundAccount);
    }

    @Nullable
    public AccountResponse getAccountById(UUID id) {
        log.debug("Getting account by id: {}", id);

        AccountEntity foundAccount = this.accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account not found")
        );

        return accountMapper.toResponse(foundAccount);
    }

    public AccountEntity createAccount(String name) throws IllegalArgumentException {
        log.info("Creating account: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        AccountEntity foundAccount = this.accountRepository.findByName(name);

        if (foundAccount != null) {
            throw new IllegalArgumentException("Account already exists with name " + name);
        }

        AccountEntity account = new AccountEntity(name);
        AccountEntity newAccount = this.accountRepository.save(account);

        log.info("Account created: {} with ID: {}", name, newAccount.getId());
        return newAccount;
    }

    public AccountResponse updateAccountName(String newName, UUID id) throws IllegalArgumentException {
        log.info("Updating account ID: {} to new name: {}", id, newName);

        if (newName == null || id == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        AccountEntity accountToUpdate = this.accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        if (newName.equals(accountToUpdate.getName())) {
            log.debug("Account name unchanged: {}", newName);
            return accountMapper.toResponse(accountToUpdate);
        }

        AccountEntity existingAccountWithName = this.accountRepository.findByName(newName);
        if (existingAccountWithName != null) {
            throw new IllegalArgumentException("Account already exists with name " + newName);
        }

        String oldName = accountToUpdate.getName();
        accountToUpdate.setName(newName);
        AccountEntity updatedAccount = this.accountRepository.save(accountToUpdate);

        log.info("Account updated: {} -> {} (ID: {})", oldName, newName, id);
        return accountMapper.toResponse(updatedAccount);
    }

    public void deleteAccount(UUID id) throws IllegalArgumentException {
        log.info("Deleting account: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Id can't be empty");
        }

        AccountEntity account = this.accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        this.accountRepository.deleteById(account.getId());
        log.info("Account deleted: {} ({})", account.getName(), id);
    }
}