package cs544;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class EmailLoggingAspect {

    @After("execution(*cs544.EmailSender.sendEmail(..))")
    public void logAfterEmailSent() {
        System.out.println(new Date() + " method= sendEmail");
    }
}
