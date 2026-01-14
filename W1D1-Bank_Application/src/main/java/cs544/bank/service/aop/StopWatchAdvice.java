package cs544.bank.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class StopWatchAdvice {

    @Around("execution(* cs544.bank.service..*(..))")
    public Object timeServiceMethods(ProceedingJoinPoint pjp) throws Throwable {

        StopWatch sw = new StopWatch();
        sw.start(pjp.getSignature().getName());

        Object result = pjp.proceed();

        sw.stop();
        long ms = sw.lastTaskInfo().getTimeMillis();

        System.out.println("Time to execute " + pjp.getSignature().getName() + " = " + ms + " ms");
        return result;
    }
}
