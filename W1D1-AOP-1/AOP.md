### Spring AOP: Aspect-Oriented Programming

#### Objective

This exercise provides practical experience with Aspect-Oriented Programming (AOP) 
using the Spring Framework. You will learn to define and apply various types of 
advice (after, around) to intercept method calls, extract information from join 
points (arguments, target objects), and implement cross-cutting concerns like 
logging and performance monitoring.

#### Instructions

1.  **Project Setup**

    - **Start with the provided base project:** Download and extract the `W1D1-AOP-1.zip` 
    attached to this assignment. This zip file contains the initial Spring Boot project structure.
    - **Add AspectJ Dependencies:** Open the `pom.xml` file in the root of the `W1D1-AOP-1` project. 
    Add the following dependencies to enable AspectJ weaving:

      ```xml
      <dependency>
          <groupId>org.aspectj</groupId>
          <artifactId>aspectjrt</artifactId>
          <version>1.9.21</version> <!-- Use a recent stable version, adjust if needed -->
      </dependency>
      <dependency>
          <groupId>org.aspectj</groupId>
          <artifactId>aspectjweaver</artifactId>
          <version>1.9.21</version> <!-- Use a recent stable version, adjust if needed -->
      </dependency>
      ```

      _Note: After adding dependencies, ensure you refresh your Maven project (e.g., right-click `pom.xml` -> Maven -> Reload project, or run `mvn clean install` from the terminal) for them to be recognized._

2.  **Understanding the Application Structure**
    ![class diagram](http://manalabs.org/videos/res/course/cs544/2025-10//images/w1d1-aop_classdiagram.jpg)
    _ The provided application consists of `CustomerService`, `EmailSender`, and `CustomerDAO` classes within the `cs544` package.
    _ `CustomerService` handles core business logic. It has injected references to `EmailSender` (for sending emails) and `CustomerDAO` (for saving customer data).
    _ When the `addCustomer()` method is called on `CustomerService`, it creates `Customer` and `Address` objects, then proceeds to save the customer via `CustomerDAO.save()` and sends a welcome email using `EmailSender.sendEmail()`.
    ![sequence diagram](http://manalabs.org/videos/res/course/cs544/2025-10//images/w1d1-aop_sequencediagram.jpg)
    _ **Run the `App.java` main method** from the `cs544` package (e.g., by right-clicking `App.java` and selecting "Run"). You should see output similar to this:

            ```
            CustomerDAO: saving customer Frank Brown
            EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
            ```

3.  **Implement After Advice for Basic Email Logging (Part a)**

    - **Task:** Create a new Spring Aspect (e.g., `EmailLoggingAspect`). Implement an `@After` advice that triggers whenever the `sendEmail` method on the `EmailSender` class is called. Your advice should simply print a log message to the console.
    - **Expected Output:** After implementing this, running your application should produce output similar to (timestamp will vary):

      ```
      CustomerDAO: saving customer Frank Brown
      EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
      Fri Jun 05 14:09:47 GMT 2009 method= sendMail
      ```

4.  **Enhance Email Log with Method Arguments (Part b)**

    - **Task:** Modify the advice created in Part 3. Enhance it to also log the email address and the message content that were passed as arguments to the `sendEmail()` method. You can achieve this by capturing method arguments in your pointcut expression and injecting them into your advice method.
    - **Expected Output:**

      ```
      CustomerDAO: saving customer Frank Brown
      EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
      Fri Jun 05 14:17:31 GMT 2009 method= sendEmail address=fbrown@acme.com message= Welcome Frank Brown as a new customer
      ```

5.  **Enhance Email Log with Target Object Information (Part c)**

    - **Task:** Further modify the advice from Part 4. This time, include the `outgoingMailServer` attribute from the `EmailSender` object itself in your log message. You can access the target object instance through the `JoinPoint` argument in your advice method.
    - **Expected Output:**

      ```
      CustomerDAO: saving customer Frank Brown
      EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
      Fri Jun 05 14:22:24 GMT 2009 method= sendEmail address=fbrown@acme.com message= Welcome Frank Brown as a new customer outgoing mail server = smtp.acme.com
      ```

6.  **Implement Around Advice for Performance Monitoring (Part d)**

    - **Task:** Create a _new_ Spring Aspect (e.g., `PerformanceAspect`). Implement an `@Around` advice that measures the execution duration of _all_ public method calls within the `CustomerDAO` object. Use Spring's `org.springframework.util.StopWatch` utility for this. Log the method name and its execution time to the console.
    - **Example `StopWatch` usage within an `@Around` advice:**

      ```java
      import org.springframework.util.StopWatch;
      import org.aspectj.lang.ProceedingJoinPoint;
      import org.aspectj.lang.annotation.Around;
      import org.aspectj.lang.annotation.Aspect;
      import org.springframework.stereotype.Component;

      @Aspect
      @Component
      public class PerformanceAspect {

          @Around("execution(* cs544.CustomerDAO.*(..))")
          public Object measureMethodExecutionTime(ProceedingJoinPoint call ) throws Throwable {
              StopWatch sw = new StopWatch();
              sw.start(call.getSignature().getName());
              Object retVal = call.proceed(); // Proceed with the original method execution
              sw.stop();
              long totaltime = sw.lastTaskInfo().getTimeMillis();
              System.out.println("Time to execute " + call.getSignature().getName() + " = " + totaltime + " ms");
              return retVal;
          }
      }
      ```

    - **Expected Output:** (Note: execution time will vary)

      ```
      CustomerDAO: saving customer Frank Brown
      Time to execute save = 350 ms
      EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
      Fri Jun 05 14:30:07 GMT 2009 method= sendEmail address=fbrown@acme.com message= Welcome Frank Brown as a new customer outgoing mail server = smtp.acme.com
      ```

7.  **README.md**
    - Create a `README.md` file in the root of your project directory.
    - Clearly describe how to run your application (e.g., "Run `App.java` main method").

#### Submission

Before submission, please run `mvn clean` in your project directory (from the terminal in the `W1D1-AOP-1` root folder) to delete the `target` folder and any generated build artifacts. Then, compress your entire `W1D1-AOP-1` Spring Boot project directory (including the `src` folder, `pom.xml`, and your `README.md`) into a single `.zip` file. Submit this `.zip` file here by clicking on the upload icon.

#### Grading Criteria

This exercise will be graded out of **5 points** based on the following criteria:

- **0.5 points:** Correct project setup, including the addition of AspectJ dependencies and successful initial run of the provided application.
- **1.0 points:** Successful implementation of the basic `@After` advice for `EmailSender.sendEmail()` (Part 3).
- **1.0 points:** Correct modification of the advice to extract and log method arguments (email address and message) (Part 4).
- **1.0 points:** Proper modification of the advice to access and log information from the target object (`outgoingMailServer`) (Part 5).
- **1.0 points:** Successful implementation of an `@Around` advice using `StopWatch` to measure and log `CustomerDAO` method execution times (Part 6).
- **0.5 points:** A clear, concise, and accurate `README.md` file that explains how to run the application, demonstrates the output for each part, and includes a completion time estimate.

**Total: 5 points**
