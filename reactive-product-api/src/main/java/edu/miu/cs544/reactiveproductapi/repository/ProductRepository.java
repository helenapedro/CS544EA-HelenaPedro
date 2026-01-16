package edu.miu.cs544.reactiveproductapi.repository;

import edu.miu.cs544.reactiveproductapi.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> { }
