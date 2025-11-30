package com.financial_tracker.transaction_processing;

import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import com.financial_tracker.core.transaction.TransactionEntity;
import com.financial_tracker.transaction_processing.dto.TransactionResponse;
import com.financial_tracker.transaction_processing.dto.request.TransactionCreate;
import com.financial_tracker.transaction_processing.dto.request.TransactionUpdate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(TransactionEntity transactionEntity) {
        return new TransactionResponse(
                transactionEntity.getId(),
                transactionEntity.getAmount(),
                transactionEntity.getCurrency(),
                transactionEntity.getType(),
                transactionEntity.getCreatedAt(),
                transactionEntity.getUpdatedAt()
        );
    }

    public TransactionEntity toEntity(TransactionCreate transactionCreate, AccountEntity accountEntity, SubcategoryEntity subCategoryEntity) {
        return new TransactionEntity(
                null,
                transactionCreate.amount(),
                transactionCreate.currency(),
                transactionCreate.type(),
                accountEntity,
                subCategoryEntity
        );
    }
    public void updateEntity(TransactionUpdate transactionUpdate, TransactionEntity transactionEntity) {
        transactionEntity.setAmount(transactionUpdate.amount());
        transactionEntity.setCurrency(transactionUpdate.currency());
        transactionEntity.setType(transactionUpdate.type());
    }


}
