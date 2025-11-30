package com.financial_tracker.transaction_processing;

import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import com.financial_tracker.core.subcategory.SubcategoryRepository;
import com.financial_tracker.core.transaction.TransactionEntity;
import com.financial_tracker.core.transaction.TransactionRepository;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import com.financial_tracker.transaction_processing.dto.TransactionResponse;
import com.financial_tracker.transaction_processing.dto.request.TransactionCreate;
import com.financial_tracker.transaction_processing.dto.request.TransactionFilter;
import com.financial_tracker.transaction_processing.dto.request.TransactionUpdate;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, SubcategoryRepository subcategoryRepository, AccountRepository accountRepository, TransactionMapper transactionMapper) {

        this.transactionRepository = transactionRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    public PageResponse<TransactionResponse> getAllTransactionByAccountAndFilters(UUID accountId, TransactionFilter filters, PageRequest pageRequest) {
        log.debug("Getting all transaction by filters - page: {}, size: {}", pageRequest.page(), pageRequest.size());

        Pageable pageable = pageRequest.getPageable();

        log.debug("filters: {}", filters);

        Page<TransactionEntity> page = transactionRepository.findAllByFilters(accountId, filters.subcategoryId(), filters.categoryId(), filters.currency(), filters.type(), filters.fromDate(), filters.toDate(), pageable);

        return PageResponse.of(page.map(transactionMapper::toResponse));

    }

    @Nullable
    public TransactionResponse getTransactionByAccountIdAndId(UUID accountId, UUID transactionId) {
        log.debug("Getting transaction by id: {}", transactionId);

        if (transactionId == null) {
            throw new IllegalArgumentException("Transaction id must not be empty");
        }


        TransactionEntity foundTransaction = transactionRepository.getByAccount_IdAndId(accountId, transactionId).orElseThrow(() -> new EntityNotFoundException("Transaction not found"));


        return transactionMapper.toResponse(foundTransaction);
    }


    public TransactionResponse createTransaction(UUID accountId, TransactionCreate transactionCreate) throws IllegalArgumentException {
        log.info("Creating transaction: {}", accountId, transactionCreate);


        if (!subcategoryRepository.existsByIdAndCategory_Account_Id(transactionCreate.subcategoryId(), accountId)) {
            throw new EntityNotFoundException("Subcategory not found");
        }

        AccountEntity accountEntity = accountRepository.getReferenceById(accountId);
        SubcategoryEntity subcategoryEntity = subcategoryRepository.getReferenceById(transactionCreate.subcategoryId());

        TransactionEntity transactionEntity = transactionMapper.toEntity(transactionCreate, accountEntity, subcategoryEntity);

        TransactionEntity newTransaction = transactionRepository.save(transactionEntity);

        log.info("Transaction created with ID: {}", newTransaction.getId());
        return transactionMapper.toResponse(newTransaction);
    }

    public TransactionResponse updateTransaction(UUID accountId, UUID transactionId, TransactionUpdate transactionUpdate) {
        log.info("Updating transaction ID: {} for account: {}", transactionId, accountId);


        TransactionEntity existingTransaction = transactionRepository.getByAccount_IdAndId(accountId, transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));


        if (!existingTransaction.getSubCategory().getId().equals(transactionUpdate.subcategoryId())) {
            if (!subcategoryRepository.existsByIdAndCategory_Account_Id(transactionUpdate.subcategoryId(), accountId)) {
                throw new EntityNotFoundException("Subcategory not found for this account");
            }
            SubcategoryEntity newSubcategory = subcategoryRepository.getReferenceById(transactionUpdate.subcategoryId());
            existingTransaction.setSubCategory(newSubcategory);
        }

        transactionMapper.updateEntity(transactionUpdate, existingTransaction);

        TransactionEntity updatedTransaction = transactionRepository.save(existingTransaction);

        log.info("Transaction updated with ID: {}", updatedTransaction.getId());
        return transactionMapper.toResponse(updatedTransaction);
    }


    public void deleteTransaction(UUID accountId, UUID id) {
        log.info("Deleting transaction: {}", id);

        if (!transactionRepository.existsByAccount_IdAndId(accountId, id)) {
            throw new EntityNotFoundException("Transaction not found");
        }

        this.transactionRepository.deleteById(id);
        log.info("Transaction deleted: {} ({})", accountId, id);
    }
}