package edu.miu.cs544.reactivecontentplatform.repository.blocking;

import edu.miu.cs544.reactivecontentplatform.model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, String> {
}
