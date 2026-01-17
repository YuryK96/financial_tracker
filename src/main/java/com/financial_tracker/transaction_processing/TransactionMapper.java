package com.financial_tracker.transaction_processing;

import com.financial_tracker.category_management.SubcategoryMapper;
import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.source.SourceEntity;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import com.financial_tracker.core.transaction.TransactionEntity;
import com.financial_tracker.source_managment.SourceMapper;
import com.financial_tracker.source_managment.dto.response.SourceResponse;
import com.financial_tracker.transaction_processing.dto.TransactionResponse;
import com.financial_tracker.transaction_processing.dto.request.TransactionCreate;
import com.financial_tracker.transaction_processing.dto.request.TransactionUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionMapper {

    private static final Logger log = LoggerFactory.getLogger(TransactionMapper.class);

    public TransactionResponse toResponse(TransactionEntity transactionEntity) {



        return new TransactionResponse(
                transactionEntity.getId(),
                transactionEntity.getAmount(),
                transactionEntity.getCurrency(),
                transactionEntity.getType(),
                transactionEntity.getSourceId(),
                transactionEntity.getDescription(),
                transactionEntity.getCreatedAt(),
                transactionEntity.getUpdatedAt(),
                null,
                null
        );
    }
    public TransactionResponse toResponseWithSourceAndCategory(TransactionEntity transactionEntity) {

        SourceResponse sourceResponse = null;
        if (transactionEntity.getSource() != null) {
            sourceResponse = new SourceResponse(
                    transactionEntity.getSource().getId(),
                    transactionEntity.getSource().getName(),
                    transactionEntity.getSource().getCreatedAt(),
                    transactionEntity.getSource().getUpdatedAt()
            );
        }

        SubcategoryResponse subcategoryResponse = null;
        if (transactionEntity.getSubcategory() != null) {
            subcategoryResponse = new SubcategoryResponse(
                    transactionEntity.getSubcategory().getId(),
                    transactionEntity.getSubcategory().getName(),
                    transactionEntity.getSubcategory().getCreatedAt(),
                    transactionEntity.getSubcategory().getUpdatedAt()
            );
        }

        return new TransactionResponse(
                transactionEntity.getId(),
                transactionEntity.getAmount(),
                transactionEntity.getCurrency(),
                transactionEntity.getType(),
                transactionEntity.getSourceId(),
                transactionEntity.getDescription(),
                transactionEntity.getCreatedAt(),
                transactionEntity.getUpdatedAt(),
                sourceResponse,
                subcategoryResponse
        );
    }

    public TransactionEntity toEntity(TransactionCreate transactionCreate, AccountEntity accountEntity, SubcategoryEntity subCategoryEntity, SourceEntity source) {
        return new TransactionEntity(
                null,
                transactionCreate.amount(),
                transactionCreate.currency(),
                transactionCreate.type(),
                accountEntity,
                subCategoryEntity,
                transactionCreate.description(),
                source
        );
    }
    public void updateEntity(TransactionUpdate transactionUpdate, TransactionEntity transactionEntity) {
        log.debug("Update transaction description: {}", transactionUpdate.description());

        if (transactionUpdate.amount().isPresent()) {
            transactionEntity.setAmount(transactionUpdate.amount().get());
        }

        if (transactionUpdate.currency().isPresent()) {
            transactionEntity.setCurrency(transactionUpdate.currency().get());
        }


        if (transactionUpdate.type().isPresent()) {
            transactionEntity.setType(transactionUpdate.type().get());
        }

        if (transactionUpdate.description().isPresent()) {
            transactionEntity.setDescription(transactionUpdate.description().get());
        }
    }


}
