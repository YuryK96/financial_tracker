package com.financial_tracker.source_managment;

import com.financial_tracker.category_management.dto.response.CategoryResponseWithSubcategoriesAndCount;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.core.source.SourceEntity;
import com.financial_tracker.core.source.SourceRepository;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import com.financial_tracker.core.transaction.TransactionCount;
import com.financial_tracker.core.transaction.TransactionRepository;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import com.financial_tracker.source_managment.dto.request.CreateSource;
import com.financial_tracker.source_managment.dto.request.UpdateSource;
import com.financial_tracker.source_managment.dto.response.SourceResponse;
import com.financial_tracker.source_managment.dto.response.SourceResponseWithTransaction;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SourceService {
    private static final Logger logger = LogManager.getLogger(SourceService.class);
    private final SourceRepository sourceRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public SourceService(SourceRepository sourceRepository, AccountRepository accountRepository,TransactionRepository transactionRepository) {
        this.sourceRepository = sourceRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    public PageResponse<SourceResponseWithTransaction> findAll(PageRequest pageRequest, UUID accountId) {
        logger.info("Getting source by id - page: {}, size: {}", pageRequest.page(), pageRequest.size());

        Pageable pageable = pageRequest.getPageable();
        Page<SourceEntity> page = sourceRepository.findAllByAccount_Id(accountId,pageable);

        List<UUID> sourceIds = page.getContent().stream()
                .map(SourceEntity::getId)
                .collect(Collectors.toList());

        Map<UUID, Long> transactionCounts = sourceIds.isEmpty()
                ? Collections.emptyMap()
                : transactionRepository.findTransactionCountsInSources(sourceIds)
                .stream()
                .collect(Collectors.toMap(
                        TransactionCount::itemId,
                        TransactionCount::count
                ));

        Page<SourceResponseWithTransaction> resultPage = page.map(source ->
                SourceMapper.toResponse(source, transactionCounts)
        );

        return PageResponse.of(resultPage);

    }


    public SourceResponse getSourceById(UUID id, UUID accountId) {
        logger.info("Getting source by id: {}", id);
        SourceEntity sourceEntity = sourceRepository.findByIdAndAccount_Id(id, accountId).orElseThrow(() -> new EntityNotFoundException("Source not found"));

        return SourceMapper.toResponse(sourceEntity);
    }


    public SourceResponse createSource(CreateSource createSource, UUID accountId) {
        logger.info("Create source: {}, account: {}", createSource.name(), accountId);

        if (sourceRepository.existsByNameAndAccount_Id(createSource.name(), accountId)) {

            throw new IllegalArgumentException("Source already exists");

        }

        SourceEntity newSource = SourceMapper.toEntity(createSource, accountRepository.getReferenceById(accountId));

        SourceEntity savedSource = sourceRepository.save(newSource);
        return SourceMapper.toResponse(savedSource);

    }


    public SourceResponse updateSource(UpdateSource updateSource, UUID sourceId, UUID accountId) {
        logger.info("Update source: {}, account: {}", updateSource.name(), accountId);
        if (sourceRepository.existsByNameAndAccount_Id(updateSource.name(), accountId)) {
            throw new IllegalArgumentException("Source already exists");
        }

        SourceEntity foundSource = this.sourceRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("Source not found"));

        SourceMapper.updateEntity(foundSource, updateSource);
        logger.debug("Updated source: {}", foundSource.getName());
        SourceEntity savedSource = sourceRepository.save(foundSource);

        return SourceMapper.toResponse(savedSource);
    }

    public void deleteSource(UUID sourceId, UUID accountId) {
        logger.info("Delete source: {}, account: {}", sourceId, accountId);
        if (!sourceRepository.existsByIdAndAccountId(sourceId, accountId)) {
            throw new IllegalArgumentException("Source not found");
        }

        sourceRepository.deleteById(sourceId);
    }


}
