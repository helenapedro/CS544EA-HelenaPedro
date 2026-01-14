package cs544;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class EmailLoggingAspect {

    // Task 3 — Part (a): After Advice for Email Logging
    @After("execution(* cs544.EmailSender.sendEmail(..))")
    public void logAfterEmailSent() {
        System.out.println(new Date() + " method= sendEmail");
    }

    // Task 4 — Part (b): Log Method Arguments
    @After("execution(* cs544.EmailSender.sendEmail(..)) && args(email, message)")
    public void logAfterEmailSent(String email, String message) {
        System.out.println(
                new Date() +
                        " method= sendEmail address=" + email +
                        " message= " + message
        );
    }
}
