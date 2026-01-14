# CS544 – AOP Homework (W1D1)

## How to Run
Run the `App.java` main method.

---

## Part (a) – After Advice: Basic Email Logging

**Description:**  
Logs a message after `EmailSender.sendEmail()` is executed.

**Console Output:**
CustomerDAO: saving customer Frank Brown
EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
Wed Jan 14 11:41:10 CST 2026 method= sendEmail
---

## Part (b) – After Advice with Method Arguments

**Description:**  
Logs the email address and message passed to `sendEmail()`.

**Console Output:**
CustomerDAO: saving customer Frank Brown
EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
Wed Jan 14 11:42:06 CST 2026 method= sendEmail address=fbrown@acme.com message= Welcome Frank Brown as a new customer
---

## Part (c) – After Advice with Target Object Data

**Description:**  
Logs the outgoing mail server using the target object via `JoinPoint`.

**Console Output:**
CustomerDAO: saving customer Frank Brown
EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
Wed Jan 14 11:43:16 CST 2026 method= sendEmail address=fbrown@acme.com message= Welcome Frank Brown as a new customer outgoing mail server = smtp.acme.com
---

## Part (d) – Around Advice: Performance Monitoring

**Description:**  
Measures execution time of all public methods in `CustomerDAO` using `StopWatch`.

**Console Output:**
CustomerDAO: saving customer Frank Brown
Time to execute save = 351 ms
EmailSender: sending 'Welcome Frank Brown as a new customer' to fbrown@acme.com
Wed Jan 14 11:46:11 CST 2026 method= sendEmail address=fbrown@acme.com 
message= Welcome Frank Brown as a new customer outgoing mail 
server = smtp.acme.com
---

## Estimated Completion Time
~2 hours

