package com.financial_tracker.ui;

import com.financial_tracker.domain.Account;
import com.financial_tracker.service.AccountService;
import com.financial_tracker.service.TransactionService;

import java.util.Scanner;

public class MenuFactory {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Scanner scanner;

    public MenuFactory(AccountService accountService, TransactionService transactionService, Scanner scanner) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.scanner = scanner;
    }


    public AccountMenu createAccountMenu(Account account) {
        return new AccountMenu(scanner, account, accountService, this);
    }

    public ConsoleMenu createConsoleMenu() {
        return new ConsoleMenu(scanner, accountService, this);
    }

    public TransactionMenu createTransactionMenu(Account account) {
        return new TransactionMenu(scanner, account, transactionService);
    }
}
