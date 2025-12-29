package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.category_management.dto.request.CategoryCreate;
import com.financial_tracker.category_management.dto.request.CategoryUpdate;
import com.financial_tracker.core.category.CategoryEntity;
import com.financial_tracker.core.category.CategoryRepository;
import com.financial_tracker.core.subcategory.SubCategoryConstants;
import com.financial_tracker.core.subcategory.SubcategoryEntity;
import com.financial_tracker.core.subcategory.SubcategoryRepository;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubcategoryService {

    private static final Logger log = LoggerFactory.getLogger(SubcategoryService.class);

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    public SubcategoryService(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository, SubcategoryMapper subcategoryMapper) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryMapper = subcategoryMapper;
    }

    public PageResponse<SubcategoryResponse> getCategoriesByAccountId(UUID accountId, UUID categoryId, PageRequest pageRequest) {
        log.debug("Getting subcategories for account: {}, category: {}, page: {}, size: {}",
                accountId, categoryId, pageRequest.page(), pageRequest.size());

        if (accountId == null || categoryId == null) {
            throw new IllegalArgumentException("accountId or categoryId cannot be null");
        }

        Page<SubcategoryEntity> subcategories = subcategoryRepository.findByCategory_IdAndCategory_Account_Id(categoryId, accountId, pageRequest.getPageable());

        return PageResponse.of(subcategories.map(subcategoryMapper::toResponse));
    }

    public SubcategoryResponse createSubcategory(UUID accountId, UUID categoryId, CategoryCreate categoryCreate) {
        log.info("Creating subcategory: {} for account: {}, category: {}",
                categoryCreate.name(), accountId, categoryId);

        CategoryEntity foundCategory = categoryRepository.findByIdAndAccount_Id( categoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        SubcategoryEntity newSubcategory = new SubcategoryEntity(
                categoryCreate.name(),
                foundCategory
        );

        SubcategoryEntity createdCategory = subcategoryRepository.save(newSubcategory);

        log.info("Subcategory created: {} with ID: {}", categoryCreate.name(), createdCategory.getId());
        return subcategoryMapper.toResponse(createdCategory);
    }

    public SubcategoryResponse updateCategory(UUID accountId, UUID subCategoryId, CategoryUpdate categoryUpdate) {
        log.info("Updating subcategory ID: {} to new name: {}", subCategoryId, categoryUpdate.name());

        if (accountId == null || subCategoryId == null) {
            throw new IllegalArgumentException("accountId or subCategoryId cannot be null");
        }

        SubcategoryEntity foundSubcategory = subcategoryRepository.findByIdAndCategory_Account_Id(subCategoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found"));


        if(foundSubcategory.getName().equals( SubCategoryConstants.DEFAULT_SUBCATEGORY_NAME)){
            throw new IllegalArgumentException("Default subcategory immutable");
        }

        foundSubcategory.setName(categoryUpdate.name());

        SubcategoryEntity updatedCategory = subcategoryRepository.save(foundSubcategory);

        log.info("Subcategory updated: {} with new name: {}", subCategoryId, categoryUpdate.name());
        return subcategoryMapper.toResponse(updatedCategory);
    }

    public void deleteSubcategory(UUID accountId, UUID subcategoryId) {
        log.info("Deleting subcategory: {}", subcategoryId);

        if (accountId == null || subcategoryId == null) {
            throw new IllegalArgumentException("accountId or subcategoryId cannot be null");
        }

        SubcategoryEntity foundSubcategory = subcategoryRepository.findByIdAndCategory_Account_Id(subcategoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found"));

        if(foundSubcategory.getName().equals(SubCategoryConstants.DEFAULT_SUBCATEGORY_NAME)){
            log.warn("found subcategory was deleted: {}", foundSubcategory.getName());
            throw new IllegalArgumentException("Default subcategory immutable");
        }

        subcategoryRepository.delete(foundSubcategory);

        log.info("Subcategory deleted: {}", subcategoryId);
    }
}