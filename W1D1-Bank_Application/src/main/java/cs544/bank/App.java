package cs544.bank;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cs544.bank.domain.Account;
import cs544.bank.domain.AccountEntry;
import cs544.bank.domain.Customer;
import cs544.bank.service.IAccountService;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private IAccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        // create 2 accounts;
        accountService.createAccount(1263862, "Frank Brown");
        accountService.createAccount(4253892, "John Doe");

        // use account 1;
        accountService.deposit(1263862, 240);
        accountService.deposit(1263862, 529);
        accountService.withdrawEuros(1263862, 230);

        // use account 2;
        accountService.deposit(4253892, 12450);
        accountService.depositEuros(4253892, 200);
        accountService.transferFunds(4253892, 1263862, 100, "payment of invoice 10232");

        // show balances
        Collection<Account> accountlist = accountService.getAllAccounts();
        for (Account account : accountlist) {
            Customer customer = account.getCustomer();
            System.out.println("Statement For Account: " + account.getAccountnumber());
            System.out.println("Account Holder: " + customer.getName());
            System.out.println("-Date--------------------------Description-------------------Amount-------------");
            for (AccountEntry entry : account.getEntryList()) {
                System.out.printf("%30s%30s%20.2f\n",
                        entry.getDate().toString(),
                        entry.getDescription(),
                        entry.getAmount());
            }
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%60s%20.2f\n\n", "Current Balance:", account.getBalance());
        }
    }
}