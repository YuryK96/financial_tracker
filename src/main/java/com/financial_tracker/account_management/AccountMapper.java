package com.financial_tracker.account_management;

import com.financial_tracker.account_management.dto.AccountResponse;
import com.financial_tracker.core.account.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(AccountEntity accountEntity) {
        return new AccountResponse(
                accountEntity.getId(),
                accountEntity.getName(),
                accountEntity.getCreatedAt(),
                accountEntity.getUpdatedAt()
        );
    }


}
