package edu.miu.cs544.reactivecontentplatform.aop;

import edu.miu.cs544.reactivecontentplatform.model.Blog;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Aspect
@Component
public class BlogAuditAspect {

    // Intercept BlogService.findById(String)
    @Pointcut("execution(reactor.core.publisher.Mono<java.util.Optional<edu.miu.cs544.reactivecontentplatform.model.Blog>> " +
            "edu.miu.cs544.reactivecontentplatform.service.BlogService.findById(String)) && args(id)")
    public void blogFindById(String id) {}

    @AfterReturning(pointcut = "blogFindById(id)", returning = "result")
    public void logFindById(String id, Mono<Optional<Blog>> result) {
        result.subscribe(opt -> {
            if (opt.isPresent()) {
                Blog b = opt.get();
                System.out.println("[AUDIT] Blog searched by id=" + id +
                        " -> FOUND (title=" + b.getTitle() + ", author=" + b.getAuthor() + ")");
            } else {
                System.out.println("[AUDIT] Blog searched by id=" + id + " -> NOT FOUND");
            }
        });
    }
}
