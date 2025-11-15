import com.financial_tracker.repository.AccountRepository;
import com.financial_tracker.repository.AccountRepositoryCash;
import com.financial_tracker.repository.TransactionRepository;
import com.financial_tracker.repository.TransactionRepositoryCash;
import com.financial_tracker.service.AccountService;
import com.financial_tracker.service.TransactionService;
import com.financial_tracker.ui.ConsoleMenu;
import com.financial_tracker.ui.MenuFactory;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepositoryCash();
        TransactionRepository transactionRepository = new TransactionRepositoryCash();
        AccountService accountService = new AccountService(accountRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, accountService);
        MenuFactory menuFactory = new MenuFactory(accountService,transactionService,scanner);
        ConsoleMenu consoleMenu = new ConsoleMenu(scanner, accountService, menuFactory);

        consoleMenu.showMenu();
    }
}
