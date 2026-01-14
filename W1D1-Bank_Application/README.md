# W1D1 – Bank Application (Spring Boot + DI + AOP)

## How to Run
```bash
mvn clean spring-boot:run

(or run App.java)
```

## Dependency Injection (DI)

The following components are managed by Spring and injected into AccountService:
- AccountDAO (@Repository)
- CurrencyConverter (@Component)
- JMSSender (@Component)
- Logger (@Component)
*IAccountService* is autowired into App (CommandLineRunner).

## AOP Demonstration
### AOP Task 1 – DAO Logging (@Before)
Evidence that every DAO method call is logged:
```bash
BankLogger : DAO call: AccountDAO.saveAccount(..) args=[cs544.bank.domain.Account@...]
BankLogger : DAO call: AccountDAO.loadAccount(..) args=[1263862]
BankLogger : DAO call: AccountDAO.updateAccount(..) args=[cs544.bank.domain.Account@...]
```

### AOP Task 2 – Service Timing (@Around with StopWatch)
Evidence that service methods are timed:
```bash
Time to execute createAccount = 2 ms
Time to execute deposit = 2 ms
Time to execute withdrawEuros = 1 ms
Time to execute transferFunds = 2 ms
Time to execute getAllAccounts = 0 ms
```

### AOP Task 3 – JMS Logging (@After / @AfterReturning)
Evidence that JMS messages are logged when sent:
```bash
JMSSender: sending JMS message =Deposit of $ 12450.0 to account with accountNumber= 4253892
BankLogger : JMS sent: [Deposit of $ 12450.0 to account with accountNumber= 4253892]
```
### Application Output (Bank Statements)
At the end of execution, the app prints the account statements and balances