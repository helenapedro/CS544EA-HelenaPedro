package cs544.bank.service.aop;

import cs544.bank.logging.ILogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class JMSLogAdvice {
    private final ILogger logger;

    public JMSLogAdvice(ILogger logger) {
        this.logger = logger;
    }

    @After("execution(* cs544.bank.jms.JMSSender.sendJMSMessage(..))")
    public void logJms(JoinPoint jp) {
        logger.log("JMS sent: " + Arrays.toString(jp.getArgs()));
    }
}
