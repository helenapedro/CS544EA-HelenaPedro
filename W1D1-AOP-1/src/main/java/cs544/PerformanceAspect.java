package cs544;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

// Task 6 — Part (d): Around Advice for Performance Monitoring
@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* cs544.CustomerDAO.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

        StopWatch sw = new StopWatch();
        sw.start(pjp.getSignature().getName());

        Object result = pjp.proceed(); // VERY IMPORTANT

        sw.stop();
        System.out.println(
                "Time to execute " +
                        pjp.getSignature().getName() +
                        " = " +
                        sw.getTotalTimeMillis() +
                        " ms"
        );

        return result;
    }
}
