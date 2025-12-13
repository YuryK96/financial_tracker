package com.financial_tracker.category_management;


import com.financial_tracker.auth.CustomUserDetails;
import com.financial_tracker.category_management.dto.request.CategoryCreate;
import com.financial_tracker.category_management.dto.request.CategoryUpdate;
import com.financial_tracker.category_management.dto.response.CategoryResponse;
import com.financial_tracker.category_management.dto.response.CategoryResponseWithSubcategories;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("category")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/{accountId}")
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories
            (@PathVariable("accountId") UUID accountId,
             PageRequest pageRequest,
             @AuthenticationPrincipal CustomUserDetails customUserDetails) {


        log.info("Getting all categories, userId: {}", customUserDetails.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesByAccountId(accountId, pageRequest));
    }

    @GetMapping("/{accountId}/subcategories")
    public ResponseEntity<PageResponse<CategoryResponseWithSubcategories>> getAllCategoriesWithSubcategories
            (@PathVariable("accountId") UUID accountId,
             PageRequest pageRequest) {

        log.info("Getting all categories with subcategories");

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesWithSubcategoriesByAccountId(accountId, pageRequest));
    }

    @GetMapping("/{accountId}/category/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryByAccountId
            (@PathVariable("accountId") UUID accountId,
             @PathVariable("categoryId") UUID categoryId,
             PageRequest pageRequest) {

        log.info("Getting category");

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryByAccountId(accountId, categoryId));
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<CategoryResponse> createCategory
            (@PathVariable("accountId") UUID accountId,
             @RequestBody CategoryCreate categoryCreate) {

        log.info("Create category: ", categoryCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(accountId, categoryCreate));
    }

    @PutMapping("/{accountId}/category/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory
            (@PathVariable("accountId") UUID accountId,
             @PathVariable("categoryId") UUID categoryId,
             @RequestBody CategoryUpdate categoryUpdate) {

        log.info("Update category: ", categoryUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(accountId, categoryId, categoryUpdate));
    }

    @DeleteMapping("/{accountId}/category/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("accountId") UUID accountId,
                                                   @PathVariable("categoryId") UUID categoryId) {
        log.info("Deleting category by id {}", accountId, categoryId);
        categoryService.deleteCategory(accountId, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
