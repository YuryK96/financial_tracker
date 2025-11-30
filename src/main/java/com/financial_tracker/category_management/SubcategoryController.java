package com.financial_tracker.category_management;


import com.financial_tracker.category_management.dto.SubcategoryResponse;
import com.financial_tracker.category_management.dto.request.CategoryCreate;
import com.financial_tracker.category_management.dto.request.CategoryUpdate;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/{accountId}/category/{categoryId}")
    public ResponseEntity<PageResponse<SubcategoryResponse>> getAllSubcategoriesByCategory
            (
                    @PathVariable("accountId") UUID accountId,
                    @PathVariable("categoryId") UUID categoryId,
                    PageRequest pageRequest) {

        log.info("Getting all subcategories for category {}", categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(subcategoryService.getCategoriesByAccountId(accountId, categoryId, pageRequest));
    }

    @PostMapping("/{accountId}/category/{categoryId}")
    public ResponseEntity<SubcategoryResponse> createSubcategory
            (@PathVariable("accountId") UUID accountId,
             @PathVariable("categoryId") UUID categoryId,
             @RequestBody CategoryCreate categoryCreate) {

        log.info("Create subcategory: ", categoryCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(subcategoryService.createSubcategory(accountId, categoryId, categoryCreate));
    }

    @PutMapping("/{accountId}/subcategory/{subcategoryId}")
    public ResponseEntity<SubcategoryResponse> updateSubcategory
            (@PathVariable("accountId") UUID accountId,
             @PathVariable("subcategoryId") UUID subcategoryId,
             @RequestBody CategoryUpdate categoryUpdate) {

        log.info("Update subcategory: ", categoryUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(subcategoryService.updateCategory(accountId, subcategoryId, categoryUpdate));
    }

    @DeleteMapping("/{accountId}/subcategory/{subcategoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("accountId") UUID accountId,
                                                   @PathVariable("subcategoryId") UUID subcategoryId) {
        log.info("Deleting subcategory by id {}", accountId, subcategoryId);
        subcategoryService.deleteSubcategory(accountId, subcategoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
