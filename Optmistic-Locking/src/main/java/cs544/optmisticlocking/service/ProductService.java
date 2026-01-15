package cs544.optmisticlocking.service;

import cs544.optmisticlocking.domain.Product;
import cs544.optmisticlocking.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public Long createProduct(String name, double price) {
        Product product = new Product(null, name, price, null);
        return productRepository.save(product).getId();
    }

    public Optional<Product> retrieveProduct(Long id) {
        return productRepository.findById(id);
    }

    public void updateProductPrice(Long productId, double newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {}

        product.setPrice(newPrice);
    }
}
