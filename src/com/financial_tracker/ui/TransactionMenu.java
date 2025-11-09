package com.financial_tracker.ui;

import com.financial_tracker.domain.*;
import com.financial_tracker.service.TransactionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TransactionMenu extends Menu {
    private final TransactionService transactionService;
    private final Account account;

    public TransactionMenu(Scanner scanner, Account account, TransactionService transactionService) {
        this.transactionService = transactionService;
        this.account = account;
        super(scanner);

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

    private @Nullable Currency choiseCurrency() {
        printCurrencyOptions();

        while (true) {
            try {
                System.out.print("Choose option: ");
                int option = scanner.nextInt();

                if (option == 0) {
                    return null;
                }

                if (option >= 1 && option <= 3) {
                    return switch (option) {
                        case 1 -> Currency.USD;
                        case 2 -> Currency.EU;
                        case 3 -> Currency.BYN;
                        default -> throw new IllegalStateException("Unexpected value: " + option);
                    };
                }
                printCurrencyOptions();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Incorrect input");
                printCurrencyOptions();
            }
        }

    }

    private double setAmount() {
        while (true) {
            try {
                System.out.print("Set amount: ");
                double amount = scanner.nextDouble();

                if (amount <= 0) {
                    System.out.println("Amount cant be lower zero");
                    scanner.next();
                    continue;
                }

                return amount;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Incorrect input");
            }
        }

    }

    public SubCategory choseCategory(@NotNull List<Category> categories) {
        printCategoryOptions(categories);
        int categoriesSize = categories.size();
        int optionBack = categoriesSize + 1;
        while (true) {
            try {
                System.out.print("Choose option: ");
                int option = scanner.nextInt();

                if (option == optionBack) {
                    return null;
                }

                if (option >= 0 && option <= categoriesSize) {
                    Category category = categories.get(option);
                    SubCategory subCategory = choseSubCategory(category.getSubCategories());
                    return subCategory;
                }
                printCategoryOptions(categories);
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Incorrect input");
                printCategoryOptions(categories);
            }
        }
    }

    public SubCategory choseSubCategory(List<SubCategory> subCategoryList) {
        printSubCategoryOptions(subCategoryList);
        int categoriesSize = subCategoryList.size();
        int optionBack = categoriesSize + 1;
        while (true) {
            try {
                System.out.print("Choose option: ");
                int option = scanner.nextInt();

                if (option == optionBack) {
                    return null;
                }

                if (option >= 0 && option <= categoriesSize) {
                    SubCategory subCategory = subCategoryList.get(option);
                    return subCategory;
                }
                printSubCategoryOptions(subCategoryList);
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Incorrect input");
                printSubCategoryOptions(subCategoryList);
            }
        }
    }


    @Override
    protected void processOption(int option) {
        switch (option) {
            case 1 -> addIncomeTransaction();
            case 2 -> addExpenseTransaction();
            case 3 -> deleteTransaction();
            case 4 -> showTransactionHistory();
        }
    }

    private void addIncomeTransaction() {

        double amount = setAmount();


        Currency currency = choiseCurrency();

        if (currency == null) {
            return;
        }

        SubCategory subCategory = choseCategory(account.getCategories());

        if (subCategory == null) {
            return;
        }

        CategorizedTransaction incomeTransaction = new Income(amount, currency, subCategory, account.getId());
        CategorizedTransaction createdTransaction = transactionService.createTransaction(incomeTransaction, account.getId());

        printCreatedTransaction(createdTransaction, TransactionType.INCOME);


    }

    private void addExpenseTransaction() {
        double amount = setAmount();


        Currency currency = choiseCurrency();

        if (currency == null) {
            return;
        }

        SubCategory subCategory = choseCategory(account.getCategories());

        if (subCategory == null) {
            return;
        }

        CategorizedTransaction expenseTransaction = new Expense(amount, currency, subCategory, account.getId());
        CategorizedTransaction createdTransaction = transactionService.createTransaction(expenseTransaction, account.getId());

        printCreatedTransaction(createdTransaction, TransactionType.EXPENSE);

    }

    private void showTransactionHistory() {
        List<UUID> transactionIds = account.getTransactionIds();
        System.out.println("----- Transaction history: " + transactionIds.size() + " ------");
        for (UUID id : transactionIds) {
            System.out.println(id);
            System.out.println("                 ↓");
        }
    }

    private void deleteTransaction() {

        System.out.print("Give id transaction for delete: ");
        try {
            this.scanner.nextLine();
            UUID inputId = UUID.fromString(this.scanner.nextLine());

            boolean isDeleted = transactionService.deleteTransaction(inputId, account.getId());

            if (isDeleted) {
                System.out.println("Transaction was successfully deleted");
            } else {
                System.out.println("Transaction wasnt deleted");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Transaction wasnt deleted");
            System.out.println("ERROR: " + e.getMessage());

        }


    }

    @Override
    protected void printMenuOptions() {
        System.out.println("-----Transactions for account: " + account.getName() + " ------");
        System.out.println("1. Add new Income transaction");
        System.out.println("2. Add new Expense transaction");
        System.out.println("3. Delete transaction");
        System.out.println("4. Transaction history");
        System.out.println("0. Back");
    }


    private void printCurrencyOptions() {
        System.out.println("-----Currency for transaction------");
        System.out.println("1. USD");
        System.out.println("2. EU");
        System.out.println("3. BYN");
        System.out.println("0. Back");
    }

    private void printCreatedTransaction(CategorizedTransaction createdTransaction, TransactionType type) {
        SubCategory subCategoryNewTransaction = createdTransaction.getSubCategory();

        System.out.println("---------------------------------");
        System.out.println(type + " Transaction was added successfully: ");
        System.out.println("  id: " + createdTransaction.getId());
        System.out.println("  amount: " + createdTransaction.getAmount());
        System.out.println("  currency: " + createdTransaction.getCurrency());
        System.out.println("  category: " + subCategoryNewTransaction.getCategory().getName());
        System.out.println("  sub category: " + subCategoryNewTransaction.getName());
        System.out.println("---------------------------------");
    }

    private void printCategoryOptions(@NotNull List<Category> categoryList) {

        System.out.println("-----Account Categories: " + categoryList.size() + " -------");

        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println(i + ". " + categoryList.get(i).getName());
        }
        System.out.println(categoryList.size() + 1 + ". " + "Back");
    }

    private void printSubCategoryOptions(List<SubCategory> subCategoryList) {
        System.out.println("-----Account Sub categories: " + subCategoryList.size() + " -------");

        for (int i = 0; i < subCategoryList.size(); i++) {
            System.out.println(i + ". " + subCategoryList.get(i).getName());
        }
        System.out.println(subCategoryList.size() + 1 + ". " + "Back");
    }
}
