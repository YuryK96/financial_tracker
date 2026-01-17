package com.financial_tracker.source_managment;

import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.source.SourceEntity;
import com.financial_tracker.source_managment.dto.request.CreateSource;
import com.financial_tracker.source_managment.dto.request.UpdateSource;
import com.financial_tracker.source_managment.dto.response.SourceResponse;
import com.financial_tracker.source_managment.dto.response.SourceResponseWithTransaction;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class SourceMapper {

    static SourceEntity toEntity(CreateSource createSource, AccountEntity account) {
        return new SourceEntity(
                null,
                account,
                new ArrayList<>(),
                createSource.name()


        );

    }

    public static SourceResponse toResponse(SourceEntity sourceEntity) {
        return new SourceResponse(
                sourceEntity.getId(),
                sourceEntity.getName(),
                sourceEntity.getCreatedAt(),
                sourceEntity.getUpdatedAt()
        );
    }
    public static SourceResponseWithTransaction toResponse(SourceEntity sourceEntity, Map<UUID, Long> transactionCounts) {
        return new SourceResponseWithTransaction(
                sourceEntity.getId(),
                sourceEntity.getName(),
                transactionCounts.get(sourceEntity.getId()),
                sourceEntity.getCreatedAt(),
                sourceEntity.getUpdatedAt()
        );
    }

    static void updateEntity(SourceEntity sourceEntity, UpdateSource updateSource) {
        sourceEntity.setName(updateSource.name());

    }
}
