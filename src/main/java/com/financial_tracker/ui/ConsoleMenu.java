package com.financial_tracker.ui;

import com.financial_tracker.account_management.dto.Account;
import com.financial_tracker.account_management.AccountService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu extends Menu {
    private final AccountService accountService;
    private final MenuFactory menuFactory;

    public ConsoleMenu(Scanner scanner, AccountService accountService, MenuFactory menuFactory) {
        super(scanner);
        this.accountService = accountService;
        this.menuFactory = menuFactory;
    }

    public void showMenu() {
        printMenuOptions();
        while (true) {
            try {
                System.out.print("Choose option: ");
                int option = scanner.nextInt();

                if (option == 0) {
                    return;
                }

                if (option >= 1 && option <= 9) {
                    this.processOption(option);
                }
                printMenuOptions();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Incorrect input");
                printMenuOptions();
            }
        }
    }

    private void connectToAccountByName() {
        System.out.print("Write a account name: ");
        this.scanner.nextLine();
        String inputName = this.scanner.nextLine();

        Account account = this.accountService.getAccountByName(inputName);
        if (account == null) {
            System.out.println("Account with that name didnt found");
            return;
        }

        AccountMenu accountMenu = menuFactory.createAccountMenu(account);
        accountMenu.showMenu();

    }

    @Override
    protected void processOption(int option) {
        switch (option) {
            case 1 -> showAccounts();
            case 2 -> connectToAccountByName();
            case 3 -> createAccount();
            case 4 -> deletingAccount();
        }
    }

    private void showAccounts() {
        List<Account> accountList = this.accountService.getAllAccounts();
        System.out.println("-----Accounts: " + accountList.size() + " -------");
        for (Account acc : accountList) {
            System.out.println("Account name: " + acc.getName() + ", account id: " + acc.getId());
        }

    }
    private void createAccount() {
        System.out.print("Write a name for new account: ");
        this.scanner.nextLine();
        String inputName = this.scanner.nextLine();
        try {
            boolean isCreated = accountService.createAccount(inputName);

            if (!isCreated) {
                System.out.println("Account wasnt created");
            } else {
                System.out.println("Account created successfully");
            }
        }catch (IllegalArgumentException e){
            System.out.println("Account wasnt created");
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
        }

    }

    private void deletingAccount() {
        System.out.print("Write a account name for deleting: ");
        this.scanner.nextLine();
        String inputName = this.scanner.nextLine();
        try {
            boolean isDeleted = accountService.deleteAccount(inputName);

            if (!isDeleted) {
                System.out.println("Account wasnt deleted");
            } else {
                System.out.println("Account deleted successfully");
            }
        }catch (IllegalArgumentException e){
            System.out.println("Account wasnt deleted");
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
        }

    }

    @Override
    protected void printMenuOptions() {
        System.out.println("-------MAIN MENU--------");
        System.out.println("1. Show accounts");
        System.out.println("2. Connect to account by name");
        System.out.println("3. Create an account");
        System.out.println("4. Delete an account");
        System.out.println("0. Exit");
    }

}
