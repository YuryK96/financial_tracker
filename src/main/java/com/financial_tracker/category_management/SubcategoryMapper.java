package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.category_management.dto.SubcategoryResponseWithCount;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class SubcategoryMapper {

    public static SubcategoryResponse toResponse(SubcategoryEntity subcategoryEntity) {
        return new SubcategoryResponse(
                subcategoryEntity.getId(),
                subcategoryEntity.getName(),
                subcategoryEntity.getCreatedAt(),
                subcategoryEntity.getUpdatedAt()
        );
    }

    public static SubcategoryResponseWithCount toResponseWithCount(
            SubcategoryEntity subcategoryEntity,
            Long transactionCount) {

        return new SubcategoryResponseWithCount(
                subcategoryEntity.getId(),
                subcategoryEntity.getName(),
                transactionCount,
                subcategoryEntity.getCreatedAt(),
                subcategoryEntity.getUpdatedAt()
        );
    }
}