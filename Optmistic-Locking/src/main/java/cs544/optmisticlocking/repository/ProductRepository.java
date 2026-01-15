package cs544.optmisticlocking.repository;

import cs544.optmisticlocking.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
