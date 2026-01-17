package edu.miu.cs544.reactivecontentplatform.service;

import edu.miu.cs544.reactivecontentplatform.model.Post;
import edu.miu.cs544.reactivecontentplatform.repository.reactive.PostRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Instant;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final Sinks.Many<Post> liveFeedSink =
            Sinks.many().multicast().onBackpressureBuffer();

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Flux<Post> findPostsByBlogId(String blogId) {
        return postRepository.findByBlogIdOrderByCreatedAtAsc(blogId);
    }

    public Mono<Post> createPost(Post post) {
        post.setId(null);
        if (post.getCreatedAt() == null) {
            post.setCreatedAt(Instant.now());
        }
        return postRepository.save(post)
                .doOnNext(saved -> liveFeedSink.tryEmitNext(saved));
    }

    public Flux<Post> liveFeed() {
        return liveFeedSink.asFlux();
    }
}
