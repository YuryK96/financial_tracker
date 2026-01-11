package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class SubcategoryMapper {

    public SubcategoryResponse toResponse(SubcategoryEntity subcategoryEntity) {
        return new SubcategoryResponse(
                subcategoryEntity.getId(),
                subcategoryEntity.getName(),
                subcategoryEntity.getTransactionCount(),
                subcategoryEntity.getCreatedAt(),
                subcategoryEntity.getUpdatedAt()
        );
    }


}
