package cs544.optmisticlocking;

import cs544.optmisticlocking.repository.ProductRepository;
import cs544.optmisticlocking.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootApplication
public class OptmisticLockingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OptmisticLockingApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ProductService productService) {
        return args -> {
            Long productId = productService.createProduct("Laptop", 120.0);
            System.out.println("Created productId=" + productId);

            // Scenario 1: successful update
            productService.updateProductPrice(productId, 125.0);
            System.out.println("After update #1 => " + productService.retrieveProduct(productId).orElseThrow());

            // Scenario 2: optimistic locking failure (2 threads)
            Thread thread1 = new Thread(() -> {
                try {
                    productService.updateProductPrice(productId, 130.0);
                    System.out.println("Thread1 success");
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("Optimistic locking failure in thread 1!");
                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    productService.updateProductPrice(productId, 140.0);
                    System.out.println("Thread2 success");
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("Optimistic locking failure in thread 2!");
                }
            });

            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();

            System.out.println("Final => " + productService.retrieveProduct(productId).orElseThrow());
        };
    }
}
