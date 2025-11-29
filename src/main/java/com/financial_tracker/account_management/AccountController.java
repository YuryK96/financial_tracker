package com.financial_tracker.account_management;

import com.financial_tracker.account_management.dto.AccountResponse;
import com.financial_tracker.account_management.dto.Request.AccountCreate;
import com.financial_tracker.account_management.dto.Request.AccountUpdate;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("account")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping()
    public ResponseEntity<PageResponse<AccountResponse>> getAllAccount(
            PageRequest pageRequest
    ) {
        log.info("Getting all account");
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccounts(pageRequest));

    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") UUID id) {
        log.info("Getting account by id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(id));

    }

    @GetMapping("/{name}/name")
    public ResponseEntity<AccountResponse> getAccountByName(@PathVariable("name") String name) {
        log.info("Getting account by name {}", name);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountByName(name));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable("id") UUID id) {
        log.info("Deleting account by id {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable("id") UUID id,
            @RequestBody AccountUpdate accountUpdate) {

        log.info("Updating account {}", accountUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccountName(accountUpdate.name(), id));

    }

    @PostMapping()
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountCreate accountCreate) {

        log.info("Creating account {}", accountCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountCreate.name()));

    }


}
