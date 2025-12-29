package com.financial_tracker.category_management;


import com.financial_tracker.auth.CustomUserDetails;
import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.category_management.dto.request.CategoryCreate;
import com.financial_tracker.category_management.dto.request.CategoryUpdate;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("subcategory")
public class SubcategoryController {
    private static final Logger log = LoggerFactory.getLogger(SubcategoryController.class);
    private final SubcategoryService subcategoryService;


    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<SubcategoryResponse>> getAllSubcategoriesByCategory
            (
                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                    @PathVariable("categoryId") UUID categoryId,
                    @Valid PageRequest pageRequest) {

        log.info("Getting all subcategories for category {}", categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(subcategoryService.getCategoriesByAccountId(customUserDetails.getAccountId(), categoryId, pageRequest));
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<SubcategoryResponse> createSubcategory
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @PathVariable("categoryId") UUID categoryId,
             @Valid @RequestBody CategoryCreate categoryCreate) {

        log.info("Create subcategory: ", categoryCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(subcategoryService.createSubcategory(customUserDetails.getAccountId(), categoryId, categoryCreate));
    }

    @PutMapping("/{subcategoryId}")
    public ResponseEntity<SubcategoryResponse> updateSubcategory
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @PathVariable("subcategoryId") UUID subcategoryId,
             @Valid  @RequestBody CategoryUpdate categoryUpdate) {

        log.info("Update subcategory: ", categoryUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(subcategoryService.updateCategory(customUserDetails.getAccountId(), subcategoryId, categoryUpdate));
    }

    @DeleteMapping("/{subcategoryId}")
    public ResponseEntity<Void> deleteCategoryById(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("subcategoryId") UUID subcategoryId) {

        log.info("Deleting subcategory by id {}", customUserDetails.getUserId(), subcategoryId);

        subcategoryService.deleteSubcategory(customUserDetails.getAccountId(), subcategoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
