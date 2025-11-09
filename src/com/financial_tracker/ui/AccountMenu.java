package com.financial_tracker.ui;

import com.financial_tracker.domain.Account;
import com.financial_tracker.domain.Category;
import com.financial_tracker.domain.SubCategory;
import com.financial_tracker.service.AccountService;
import org.jetbrains.annotations.NotNull;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AccountMenu extends Menu {
    private final Account account;
    private final AccountService accountService;
    private final MenuFactory menuFactory;

    public AccountMenu(Scanner scanner, Account account, AccountService accountService,MenuFactory menuFactory ) {
        super(scanner);
        this.account = account;
        this.accountService = accountService;
        this.menuFactory = menuFactory;

    }

    @Override
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

    @Override
    protected void processOption(int option) {
        switch (option) {
            case 1 -> showAccountInfo();
            case 2 -> addCategory();
            case 3 -> removeCategory();
            case 4 -> addSubCategory();
            case 5 -> removeSubCategory();
            case 6 -> goToTransactionMenu();
        }
    }

    private void goToTransactionMenu() {
        TransactionMenu transactionMenu = menuFactory.createTransactionMenu(account);
        transactionMenu.showMenu();

    }

    private void showAccountInfo() {
        System.out.println("-----Account info-----");
        System.out.println("Name: " + account.getName());
        System.out.println("Id: " + account.getId());
        showCategories();
    }



    private void addCategory() {
        System.out.print("Write a name for new category: ");
        this.scanner.nextLine();
        String inputName = this.scanner.nextLine();
        try {
            Category category = accountService.addCategoryToAccount(inputName, account.getId());

            if (category != null) {
                System.out.println("Category was overwritten");
            } else {
                System.out.println("Category created successfully");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Category wasnt created");
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
        }

    }


    private void removeCategory() {
        System.out.print("Write a category name for removing: ");
        this.scanner.nextLine();
        String inputName = this.scanner.nextLine();
        try {
            Category category = accountService.removeCategoryFromAccount(inputName, account.getId());

            if (category == null) {
                System.out.println("Category didnt found");
            } else {
                System.out.println("Removing Category was successfully");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Category didnt remove");
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
        }

    }


    private void removeSubCategory() {
        System.out.print("Write a sub category name for removing: ");
        this.scanner.nextLine();
        String subCategoryNameInput = this.scanner.nextLine();
        System.out.print("Write a category name for: ");
        String categoryNameInput = this.scanner.nextLine();
        try {
            SubCategory subCategory = accountService.removeSubCategoryFromAccount(subCategoryNameInput, account.getId(), categoryNameInput);

            if (subCategory == null) {
                System.out.println("Sub category didnt found");
            } else {
                System.out.println("Removing Sub category was successfully");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Sub category didnt remove");
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
        }

    }


    private void addSubCategory() {
        System.out.print("Write a name for new sub category: ");
        this.scanner.nextLine();
        String subCategoryNameInput = this.scanner.nextLine();
        System.out.print("Write a category name for: ");
        String categoryNameInput = this.scanner.nextLine();
        try {
            SubCategory subCategory = accountService.addSubCategoryToAccount(subCategoryNameInput, account.getId(), categoryNameInput);

            if (subCategory != null) {
                System.out.println("Sub category was overwritten");
            } else {
                System.out.println("Sub category created successfully");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Sub category wasnt created");
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
        }

    }


    private void showCategories() {
        List<Category> categories = account.getCategories();
        System.out.println("Categories: " + categories.size());
        for (Category category : categories) {
            System.out.println("  " + category.getName());
            showSubCategories(category);
        }
    }

    private void showSubCategories(@NotNull Category category) {
        List<SubCategory> subCategories = category.getSubCategories();

        if (subCategories.isEmpty()) {
            return;
        }
        System.out.println("    Sub categories: " + subCategories.size());

        for (SubCategory subCategory : subCategories) {
            System.out.println("      " + subCategory.getName());
        }
    }


    @Override
    protected void printMenuOptions() {
        System.out.println("-----ACCOUNT: " + account.getName() + " ------");
        System.out.println("1. Account info");
        System.out.println("2. Add Category");
        System.out.println("3. Remove Category");
        System.out.println("4. Add Sub category");
        System.out.println("5. Remove Sub category");
        System.out.println("6. Go to transaction menu");
        System.out.println("0. Back");
    }

}
