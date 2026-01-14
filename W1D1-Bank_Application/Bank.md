### Spring Boot: Dependency Injection and Aspect-Oriented Programming with the Bank Application

#### Objective

This exercise provides hands-on experience converting a traditional Java application into a Spring Boot application. You will implement Dependency Injection (DI) to manage components and apply Aspect-Oriented Programming (AOP) to introduce cross-cutting concerns such as logging and performance monitoring without modifying the core business logic.

#### Instructions

1.  **Project Setup and Initial Conversion**

    - **Download the starter project:** Download and extract the provided `W1D1-Bank_Application.zip` file.
      ![class diagram](http://manalabs.org/videos/res/course/cs544/2025-10//images/w1d1-bank_classdiagram.jpg)
      ![sequence diagram - create account](http://manalabs.org/videos/res/course/cs544/2025-10//images/w1d1-bank_sequencediagram-createaccount.jpg)
      ![sequence diagram - deposit](http://manalabs.org/videos/res/course/cs544/2025-10//images/w1d1-bank_sequencediagram-deposit.jpg)

    - Running it should create the following output

```log
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: createAccount with parameters accountNumber= 1263862 , customerName= Frank Brown
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: createAccount with parameters accountNumber= 4253892 , customerName= John Doe
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: deposit with parameters accountNumber= 1263862 , amount= 240.0
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: deposit with parameters accountNumber= 1263862 , amount= 529.0
CurrencyConverter: converting 230.0 dollars to euros
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: withdrawEuros with parameters accountNumber= 1263862 , amount= 230.0
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: deposit with parameters accountNumber= 4253892 , amount= 12450.0
JMSSender: sending JMS message =Deposit of $ 12450.0 to account with accountNumber= 4253892
CurrencyConverter: converting 200.0 dollars to euros
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: depositEuros with parameters accountNumber= 4253892 , amount= 200.0
Jun 12, 2009 11:45:24 PM bank.logging.Logger log
INFO: transferFunds with parameters fromAccountNumber= 4253892 , toAccountNumber= 1263862 , amount= 100.0 , description= payment of invoice 10232
Statement For Account: 4253892
Account Holder: John Doe
-Date--------------------------Description-------------------Amount-------------
  Fri Jun 12 23:45:24 GMT 2009                       deposit            12450.00
  Fri Jun 12 23:45:24 GMT 2009                       deposit              314.00
  Fri Jun 12 23:45:24 GMT 2009      payment of invoice 10232             -100.00
--------------------------------------------------------------------------------
                                            Current Balance:            12664.00

Statement For Account: 1263862
Account Holder: Frank Brown
-Date--------------------------Description-------------------Amount-------------
  Fri Jun 12 23:45:24 GMT 2009                       deposit              240.00
  Fri Jun 12 23:45:24 GMT 2009                       deposit              529.00
  Fri Jun 12 23:45:24 GMT 2009                      withdraw             -361.10
  Fri Jun 12 23:45:24 GMT 2009      payment of invoice 10232              100.00
--------------------------------------------------------------------------------
                                            Current Balance:              507.90

```

1. **Conversion**

   - **Convert to Spring Boot:**
     - Open the `pom.xml` file and add the `spring-boot-starter` dependency.
     - Transform the `App.java` class into a Spring Boot application:
       - Annotate `App.java` with `@SpringBootApplication`.
       - Modify the `main` method to use `SpringApplication.run(App.class, args);`.
       - Implement `CommandLineRunner` in `App.java` and move the original bank application logic (creating accounts, performing transactions) into its `run` method.
   - **Verify Initial State:** Run your application. The console output should match the previous output (also shown above).

2. **Dependency Injection (DI) Implementation**

   - **Identify Injectable Components:** In the `AccountService` class, observe the lines where `AccountDAO`, `CurrencyConverter`, `JMSSender`, and `Logger` objects are instantiated using `new` (e.g., `accountDAO = new AccountDAO();`).
   - **Annotate Components:**
     - Annotate `AccountDAO` with `@Repository`.
     - Annotate `CurrencyConverter` with `@Component`.
     - Annotate `JMSSender` with `@Component`.
     - Annotate `Logger` with `@Component`.
   - **Inject into `AccountService`:** Modify the `AccountService` class to inject these dependencies. You can use constructor injection or `@Autowired` on fields or setters.
   - **Inject `AccountService`:** Autowire the `IAccountService` into your `App.java` `CommandLineRunner` to obtain an instance of `AccountService` managed by Spring.
   - **Verify DI:** Run the application again. The output should remain functionally identical, but now the dependencies are managed by Spring's DI container.

3. **Aspect-Oriented Programming (AOP) Implementation**

   - **Add AOP Dependency:** Add `spring-boot-starter-aop` to your `pom.xml`.
   - **Create AOP Package:** Create a new package, e.g., `cs544.bank.service.aop`, for your advice classes.
   - **AOP Task 1: Log DAO Calls:**
     - Create an `@Aspect` class (e.g., `DAOLogAdvice`).
     - Inject `ILogger` into this aspect.
     - Define an `@Before` advice that logs (using the injected `ILogger`) every call to _any_ method within the `cs544.bank.dao` package. The log message should indicate the method being called and its arguments.
   - **AOP Task 2: Measure Service Method Duration:**
     - Create another `@Aspect` class (e.g., `StopWatchAdvice`).
     - Define an `@Around` advice that measures the execution duration of _all_ methods within the `cs544.bank.service` package.
     - Use `org.springframework.util.StopWatch` to perform the timing.
     - Log the method name and its execution time to the console (e.g., using `System.out.println` or the injected `ILogger`).
   - **AOP Task 3: Log JMS Messages:**
     - Create another `@Aspect` class (e.g., `JMSLogAdvice`).
     - Inject `ILogger` into this aspect.
     - Define an `@AfterReturning` or `@After` advice that logs (using the injected `ILogger`) every time a JMS message is sent. This advice should target the `sendJMSMessage` method in `cs544.bank.jms.JMSSender`.
   - **Clean Up `AccountService`:** To confirm your AOP aspects are working correctly, remove all direct `logger.log()` calls from the `AccountService` class. The logging should now be entirely handled by your AOP aspects.
   - **Verify AOP:** Run the application one last time. Observe the console output for your new DAO logs, service method timings, and JMS logs.

4. **README.md**
   - Create a `README.md` file in the root of your project directory.
   - Provide clear instructions on how to build and run your application.
   - Describe the expected console output, specifically highlighting how the Dependency Injection structure is now active and how the AOP aspects (DAO logging, method timing, JMS logging) are visible in the logs.

#### Submission

Before submission, ensure your project is clean by running `mvn clean` (or deleting the `target` directory manually). Then, zip your entire Spring Boot project directory (including `src`, `pom.xml`, and your `README.md` file) and submit the `.zip` file.

#### Grading Criteria

This exercise will be graded out of **5 points** based on the following:

- **1 point:** Correct project setup, including converting the existing Java project to a Spring Boot application, adding necessary dependencies, and initial successful run.
- **1.5 points:** Proper implementation of Dependency Injection, where `AccountDAO`, `CurrencyConverter`, `JMSSender`, and `Logger` are Spring-managed components injected into `AccountService`, and `AccountService` is autowired into `App.java`.
- **2 points:** Successful implementation of all three AOP tasks:
  - Logging of all DAO method calls.
  - Measuring and logging the execution time of all service layer methods.
  - Logging of all JMS messages sent.
  - Removal of direct `logger.log()` calls from `AccountService`, demonstrating AOP handling cross-cutting concerns.
- **0.5 points:** A clear and concise `README.md` file that explains how to run the application and how to interpret the output, especially regarding the DI and AOP demonstrations.

**Total: 5 points**
