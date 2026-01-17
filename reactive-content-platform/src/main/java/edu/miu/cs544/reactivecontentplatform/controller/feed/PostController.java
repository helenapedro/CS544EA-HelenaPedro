package edu.miu.cs544.reactivecontentplatform.controller.feed;

import edu.miu.cs544.reactivecontentplatform.model.Post;
import edu.miu.cs544.reactivecontentplatform.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/blogs/{blogId}/posts")
    public Flux<Post> getPostsForBlog(@PathVariable String blogId) {
        return postService.findPostsByBlogId(blogId);
    }

    @PostMapping("/blogs/{blogId}/posts")
    public Mono<ResponseEntity<Void>> createPost(@PathVariable String blogId, @RequestBody Post post) {
        post.setBlogId(blogId);
        return postService.createPost(post)
                .map(saved -> ResponseEntity.created(URI.create("/api/posts/" + saved.getId())).build());
    }

    @GetMapping(value = "/feed/posts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Post> liveFeed() {
        return postService.liveFeed();
    }
}
