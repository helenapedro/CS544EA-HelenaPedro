package edu.miu.cs544.reactivecontentplatform.repository.reactive;

import edu.miu.cs544.reactivecontentplatform.model.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Flux<Post> findByBlogIdOrderByCreatedAtAsc(String blogId);
}
