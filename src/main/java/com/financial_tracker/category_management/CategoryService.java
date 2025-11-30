package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.response.CategoryResponse;
import com.financial_tracker.category_management.dto.request.CategoryCreate;
import com.financial_tracker.category_management.dto.request.CategoryUpdate;
import com.financial_tracker.category_management.dto.response.CategoryResponseWithSubcategories;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.core.category.CategoryEntity;
import com.financial_tracker.core.category.CategoryRepository;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, AccountRepository accountRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.categoryMapper = categoryMapper;
    }

    public PageResponse<CategoryResponse> getCategoriesByAccountId(UUID accountId, PageRequest pageRequest) {
        log.debug("Getting categories for account: {}, page: {}, size: {}", accountId, pageRequest.page(), pageRequest.size());

        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }

        Page<CategoryEntity> page = categoryRepository.findByAccount_Id(accountId, pageRequest.getPageable());

        return PageResponse.of(page.map(categoryMapper::toResponse));
    }

    public PageResponse<CategoryResponseWithSubcategories> getCategoriesWithSubcategoriesByAccountId(UUID accountId, PageRequest pageRequest) {
        log.debug("Getting categories with subcategories for account: {}, page: {}, size: {}", accountId, pageRequest.page(), pageRequest.size());

        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }

        Page<CategoryEntity> page = categoryRepository.findByAccountIdWithSubcategories(accountId, pageRequest.getPageable());

        return PageResponse.of(page.map(categoryMapper::toResponseWithSubcategories));
    }

    public CategoryResponse getCategoryByAccountId(UUID accountId, UUID categoryId) {
        log.debug("Getting category by id: {} for account: {}", categoryId, accountId);

        if (accountId == null || categoryId == null) {
            throw new IllegalArgumentException("accountId or categoryId cannot be null");
        }

        CategoryEntity foundCategory = categoryRepository.findByIdAndAccount_Id( categoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        return categoryMapper.toResponse(foundCategory);
    }

    public CategoryResponse getCategoryByAccountId(UUID accountId, String name) {
        log.debug("Getting category by name: {} for account: {}", name, accountId);

        if (accountId == null || name == null) {
            throw new IllegalArgumentException("accountId or name cannot be null");
        }

        CategoryEntity foundCategory = categoryRepository.findByNameAndAccount_Id( name,accountId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        return categoryMapper.toResponse(foundCategory);
    }

    public CategoryResponse createCategory(UUID accountId, CategoryCreate categoryCreate) {
        log.info("Creating category: {} for account: {}", categoryCreate.name(), accountId);

        AccountEntity foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        CategoryEntity newCategory = new CategoryEntity(
                categoryCreate.name(),
                foundAccount
        );

        CategoryEntity createdCategory = categoryRepository.save(newCategory);

        log.info("Category created: {} with ID: {}", categoryCreate.name(), createdCategory.getId());
        return categoryMapper.toResponse(createdCategory);
    }

    public CategoryResponse updateCategory(UUID accountId, UUID categoryId, CategoryUpdate categoryUpdate) {
        log.info("Updating category ID: {} to new name: {}", categoryId, categoryUpdate.name());

        if (accountId == null || categoryId == null) {
            throw new IllegalArgumentException("accountId or categoryId cannot be null");
        }

        CategoryEntity foundCategory = categoryRepository.findByIdAndAccount_Id( categoryId,accountId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        foundCategory.setName(categoryUpdate.name());

        CategoryEntity updatedCategory = categoryRepository.save(foundCategory);

        log.info("Category updated: {} with new name: {}", categoryId, categoryUpdate.name());
        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteCategory(UUID accountId, UUID categoryId) {
        log.info("Deleting category: {}", categoryId);

        if (accountId == null || categoryId == null) {
            throw new IllegalArgumentException("accountId or categoryId cannot be null");
        }

        CategoryEntity foundCategory = categoryRepository.findByIdAndAccount_Id( categoryId,accountId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        categoryRepository.delete(foundCategory);

        log.info("Category deleted: {}", categoryId);
    }
}