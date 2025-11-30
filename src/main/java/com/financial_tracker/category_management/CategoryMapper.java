package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.response.CategoryResponse;
import com.financial_tracker.category_management.dto.response.CategoryResponseWithSubcategories;
import com.financial_tracker.core.category.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryMapper {

    private final SubcategoryMapper subcategoryMapper;


    public CategoryMapper(SubcategoryMapper subcategoryMapper) {
        this.subcategoryMapper = subcategoryMapper;
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
                        .map(subcategoryMapper::toResponse)
                        .toList()
        );
    }


}
