package cs544.bank.service.aop;

import cs544.bank.logging.ILogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class DAOLogAdvice {
    private final ILogger  logger;

    public DAOLogAdvice(ILogger logger) {
        this.logger = logger;
    }

    @Before("execution(* cs544.bank.dao..*(..))")
    public void logDaoCall(JoinPoint jp) {
        logger.log("DAO call: " + jp.getSignature().toShortString()
                + " args=" + Arrays.toString(jp.getArgs()));
    }
}
