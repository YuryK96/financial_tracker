package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.response.CategoryResponse;
import com.financial_tracker.category_management.dto.response.CategoryResponseWithSubcategories;
import com.financial_tracker.category_management.dto.response.CategoryResponseWithSubcategoriesAndCount;
import com.financial_tracker.core.category.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CategoryMapper {

    public CategoryMapper() {
    }

    public CategoryResponse toResponse(CategoryEntity categoryEntity) {
        return new CategoryResponse(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getUpdatedAt()
        );
    }

    public CategoryResponseWithSubcategories toResponseWithSubcategories(CategoryEntity categoryEntity) {
        return new CategoryResponseWithSubcategories(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getUpdatedAt(),
                categoryEntity.getSubcategories().stream()
                        .map(SubcategoryMapper::toResponse)
                        .toList()
        );
    }

    public CategoryResponseWithSubcategoriesAndCount toResponseWithSubcategories(
            CategoryEntity categoryEntity,
            Map<UUID, Long> transactionCounts) {

        return new CategoryResponseWithSubcategoriesAndCount(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getUpdatedAt(),
                categoryEntity.getSubcategories().stream()
                        .map(subcategory -> SubcategoryMapper.toResponseWithCount(
                                subcategory,
                                transactionCounts.getOrDefault(subcategory.getId(), 0L)
                        ))
                        .toList()
        );
    }
}
