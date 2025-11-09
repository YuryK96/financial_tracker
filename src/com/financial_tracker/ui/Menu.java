package com.financial_tracker.ui;

import java.util.Scanner;

abstract class Menu {
    protected final Scanner scanner;

    protected Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    protected  abstract void showMenu();
    protected abstract void processOption(int option);
    protected abstract void printMenuOptions();
}
