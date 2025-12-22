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


    @GetMapping()
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories
            (
                    PageRequest pageRequest,
                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {


        log.info("Getting all categories, userId: {}", customUserDetails.getAccountId());

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesByAccountId(customUserDetails.getAccountId(), pageRequest));
    }

    @GetMapping("/subcategories")
    public ResponseEntity<PageResponse<CategoryResponseWithSubcategories>> getAllCategoriesWithSubcategories
            (
                    PageRequest pageRequest,
                    @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {

        log.info("Getting all categories with subcategories");

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesWithSubcategoriesByAccountId(customUserDetails.getAccountId(), pageRequest));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryByAccountId
            (
                    @PathVariable("categoryId") UUID categoryId,
                    PageRequest pageRequest,
                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        log.info("Getting category");

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryByAccountId(customUserDetails.getAccountId(), categoryId));
    }

    @PostMapping()
    public ResponseEntity<CategoryResponse> createCategory
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestBody CategoryCreate categoryCreate) {

        log.info("Create category: ", categoryCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(customUserDetails.getAccountId(), categoryCreate));
    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @PathVariable("categoryId") UUID categoryId,
             @RequestBody CategoryUpdate categoryUpdate) {

        log.info("Update category: ", categoryUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(customUserDetails.getAccountId(), categoryId, categoryUpdate));
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("categoryId") UUID categoryId) {

        log.info("Deleting category by id {}", customUserDetails.getAccountId(), categoryId);

        categoryService.deleteCategory(customUserDetails.getAccountId(), categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
