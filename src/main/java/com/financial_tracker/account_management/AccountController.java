package com.financial_tracker.account_management;

import com.financial_tracker.account_management.dto.AccountResponse;
import com.financial_tracker.account_management.dto.request.AccountUpdate;
import com.financial_tracker.auth.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


//    @GetMapping()
//    public ResponseEntity<PageResponse<AccountResponse>> getAllAccount(
//            PageRequest pageRequest
//    ) {
//        log.info("Getting all account");
//        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccounts(pageRequest));
//
//    }

    @GetMapping()
    public ResponseEntity<AccountResponse> getAccountById( @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("Getting account by id {}", customUserDetails.getAccountId());
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(customUserDetails.getAccountId()));

    }

//    @GetMapping("/{name}/name")
//    public ResponseEntity<AccountResponse> getAccountByName(@PathVariable("name") String name) {
//        log.info("Getting account by name {}", name);
//        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountByName(name));
//
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccountById(@PathVariable("id") UUID id) {
//        log.info("Deleting account by id {}", id);
//        accountService.deleteAccount(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    @PutMapping()
    public ResponseEntity<AccountResponse> updateAccount(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AccountUpdate accountUpdate) {

        log.info("Updating account {}", accountUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccountName(accountUpdate.name(), customUserDetails.getAccountId()));

    }



}
