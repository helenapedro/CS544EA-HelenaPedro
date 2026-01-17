package edu.miu.cs544.reactivecontentplatform.repository.blocking;

import edu.miu.cs544.reactivecontentplatform.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
