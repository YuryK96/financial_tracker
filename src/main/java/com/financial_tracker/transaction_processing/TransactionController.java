package com.financial_tracker.transaction_processing;


import com.financial_tracker.auth.CustomUserDetails;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import com.financial_tracker.transaction_processing.dto.TransactionResponse;
import com.financial_tracker.transaction_processing.dto.request.TransactionCreate;
import com.financial_tracker.transaction_processing.dto.request.TransactionFilter;
import com.financial_tracker.transaction_processing.dto.request.TransactionQuery;
import com.financial_tracker.transaction_processing.dto.request.TransactionUpdate;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping()
    public ResponseEntity<PageResponse<TransactionResponse>> getAllTransactionByAccountAndFilters(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid  TransactionQuery transactionQuery) {

        log.info("Getting all transaction by filter {}", transactionQuery);

        TransactionFilter filters = transactionQuery.filters();
        PageRequest pagination = transactionQuery.pagination();

        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAllTransactionByAccountAndFilters(customUserDetails.getAccountId(), filters, pagination));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionByAccountIdAndId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("transactionId") UUID transactionId) {

        log.info("Getting transaction by id {}", transactionId);


        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionByAccountIdAndId(customUserDetails.getAccountId(), transactionId));
    }

    @PostMapping()
    public ResponseEntity<TransactionResponse> createTransaction
            ( @AuthenticationPrincipal CustomUserDetails customUserDetails,
              @Valid   @RequestBody TransactionCreate transactionCreate) {

        log.info("Create transaction: ", transactionCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(customUserDetails.getAccountId(), transactionCreate));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction
            (  @AuthenticationPrincipal CustomUserDetails customUserDetails,
             @PathVariable("transactionId") UUID transactionId,
               @Valid @RequestBody TransactionUpdate transactionUpdate) {

        log.info("Update transaction: ", transactionUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(transactionService.updateTransaction(customUserDetails.getAccountId(), transactionId, transactionUpdate));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransactionById( @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @PathVariable("transactionId") UUID transactionId) {
        log.info("Deleting transaction by id {}", customUserDetails.getAccountId(), transactionId);
        transactionService.deleteTransaction(customUserDetails.getAccountId(), transactionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
