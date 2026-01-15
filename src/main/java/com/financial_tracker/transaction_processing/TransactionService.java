package com.financial_tracker.transaction_processing;

import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.core.source.SourceEntity;
import com.financial_tracker.core.source.SourceRepository;
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
import jakarta.transaction.Transactional;
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
    private final SourceRepository sourceRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, SubcategoryRepository subcategoryRepository, AccountRepository accountRepository, SourceRepository sourceRepository, TransactionMapper transactionMapper) {

        this.transactionRepository = transactionRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.sourceRepository = sourceRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    public PageResponse<TransactionResponse> getAllTransactionByAccountAndFilters(UUID accountId, TransactionFilter filters, PageRequest pageRequest) {
        log.debug("Getting all transaction by filters - page: {}, size: {}", pageRequest.page(), pageRequest.size());

        Pageable pageable = pageRequest.getPageable();

        log.debug("filters: {}", filters);

        Page<TransactionEntity> page = transactionRepository.findAllByFilters(accountId, filters.subcategoryId(), filters.categoryId(), filters.currency(), filters.type(), filters.fromDate(), filters.toDate(), pageable);



        return PageResponse.of(page.map(transactionMapper::toResponseWithSourceAndCategory));

    }

    @Nullable
    public TransactionResponse getTransactionByAccountIdAndId(UUID accountId, UUID transactionId) {
        log.debug("Getting transaction by id: {}", transactionId);

        if (transactionId == null) {
            throw new IllegalArgumentException("Transaction id must not be empty");
        }


        TransactionEntity foundTransaction = transactionRepository.getByIdAndAccountIdWithSourceAndCategory(accountId, transactionId).orElseThrow(() -> new EntityNotFoundException("Transaction not found"));


        return transactionMapper.toResponseWithSourceAndCategory(foundTransaction);
    }


    public TransactionResponse createTransaction(UUID accountId, TransactionCreate transactionCreate) throws IllegalArgumentException {
        log.info("Creating transaction: {}", accountId, transactionCreate);


        if (!subcategoryRepository.existsByIdAndCategory_Account_Id(transactionCreate.subcategoryId(), accountId)) {
            throw new EntityNotFoundException("Subcategory not found");
        }

        AccountEntity accountEntity = accountRepository.getReferenceById(accountId);
        SubcategoryEntity subcategoryEntity = subcategoryRepository.getReferenceById(transactionCreate.subcategoryId());

        SourceEntity sourceEntity = sourceRepository.getReferenceById(transactionCreate.sourceId());

        TransactionEntity transactionEntity = transactionMapper.toEntity(transactionCreate, accountEntity, subcategoryEntity, sourceEntity);

        TransactionEntity newTransaction = transactionRepository.save(transactionEntity);

        log.info("Transaction created with ID: {}", newTransaction.getId());
        return transactionMapper.toResponse(newTransaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(UUID accountId, UUID transactionId, TransactionUpdate transactionUpdate) {
        log.info("Updating transaction ID: {} for account: {}, DTO: {}",
                transactionId, accountId, transactionUpdate.toString());


        TransactionEntity existingTransaction = transactionRepository.getByIdAndAccountIdWithSourceAndCategory(accountId, transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));


        updateSubcategoryIfNeed(existingTransaction, transactionUpdate, accountId);
        updateSourceIfNeed(existingTransaction, transactionUpdate, accountId);

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

    private void updateSubcategoryIfNeed(TransactionEntity transaction, TransactionUpdate transactionUpdate, UUID accountId) {

        if (!transactionUpdate.subcategoryId().isPresent()) {
            return;
        }

        if (transaction.getSubcategory().getId().equals(transactionUpdate.subcategoryId().get())) {
            return;
        }

        boolean isExist = subcategoryRepository.existsByIdAndCategory_Account_Id(transactionUpdate.subcategoryId().get(), accountId);

        if (!isExist) {
            throw new EntityNotFoundException(
                    "Subcategory not found or doesn't belong to account");

        }

        transaction.setSubcategory(subcategoryRepository.getReferenceById(transactionUpdate.subcategoryId().get()));

    }
    private void updateSourceIfNeed(TransactionEntity transaction, TransactionUpdate transactionUpdate, UUID accountId) {

        if (!transactionUpdate.sourceId().isPresent()) {
            return;
        }

        if (transaction.getSource().getId().equals(transactionUpdate.sourceId().get())) {
            return;
        }

        boolean isExist = sourceRepository.existsByIdAndAccountId(transactionUpdate.sourceId().get(), accountId);

        if (!isExist) {
            throw new EntityNotFoundException(
                    "Source not found or doesn't belong to account");

        }

        transaction.setSource(sourceRepository.getReferenceById(transactionUpdate.sourceId().get()));

    }
}