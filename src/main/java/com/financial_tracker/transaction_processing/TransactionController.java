package com.financial_tracker.transaction_processing;


import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import com.financial_tracker.transaction_processing.dto.TransactionResponse;
import com.financial_tracker.transaction_processing.dto.request.TransactionCreate;
import com.financial_tracker.transaction_processing.dto.request.TransactionFilter;
import com.financial_tracker.transaction_processing.dto.request.TransactionQuery;
import com.financial_tracker.transaction_processing.dto.request.TransactionUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/{accountId}")
    public ResponseEntity<PageResponse<TransactionResponse>> getAllTransactionByAccountAndFilters(
            @PathVariable("accountId") UUID accountId,
            TransactionQuery transactionQuery) {

        log.info("Getting all transaction by filter {}", transactionQuery);

        TransactionFilter filters = transactionQuery.filters();
        PageRequest pagination = transactionQuery.pagination();

        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAllTransactionByAccountAndFilters(accountId, filters, pagination));
    }

    @GetMapping("/{accountId}/trsnsaction/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionByAccountIdAndId(
            @PathVariable("accountId") UUID accountId,
            @PathVariable("transactionId") UUID transactionId) {

        log.info("Getting transaction by id {}", transactionId);


        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionByAccountIdAndId(accountId, transactionId));
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<TransactionResponse> createTransaction
            (@PathVariable("accountId") UUID accountId,
             @RequestBody TransactionCreate transactionCreate) {

        log.info("Create transaction: ", transactionCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(accountId, transactionCreate));
    }

    @PutMapping("/{accountId}/transaction/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction
            (@PathVariable("accountId") UUID accountId,
             @PathVariable("transactionId") UUID transactionId,
             @RequestBody TransactionUpdate transactionUpdate) {

        log.info("Update transaction: ", transactionUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(transactionService.updateTransaction(accountId, transactionId, transactionUpdate));
    }

    @DeleteMapping("/{accountId}/transaction/{transactionId}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable("accountId") UUID accountId,
                                                      @PathVariable("transactionId") UUID transactionId) {
        log.info("Deleting transaction by id {}", accountId, transactionId);
        transactionService.deleteTransaction(accountId, transactionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
