import com.financial_tracker.repository.AccountRepository;
import com.financial_tracker.repository.AccountRepositoryCash;
import com.financial_tracker.service.AccountService;
import com.financial_tracker.ui.ConsoleMenu;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepositoryCash();
        AccountService accountService = new AccountService(accountRepository);
        ConsoleMenu consoleMenu = new ConsoleMenu(scanner, accountService);

        consoleMenu.showMenu();
    }
}
