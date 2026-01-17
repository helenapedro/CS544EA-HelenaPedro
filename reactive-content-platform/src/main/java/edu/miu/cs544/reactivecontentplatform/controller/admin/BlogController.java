package edu.miu.cs544.reactivecontentplatform.controller.admin;

import edu.miu.cs544.reactivecontentplatform.model.Blog;
import edu.miu.cs544.reactivecontentplatform.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Blog>>> getAll() {
        return blogService.findAll().map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Blog>> getById(@PathVariable String id) {
        return blogService.findById(id)
                .map(opt -> opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> create(@RequestBody Blog blog) {
        return blogService.create(blog)
                .map(saved -> ResponseEntity.created(URI.create("/api/admin/blogs/" + saved.getId())).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Blog>> update(@PathVariable String id, @RequestBody Blog blog) {
        return blogService.update(id, blog)
                .map(opt -> opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return blogService.delete(id)
                .map(deleted -> deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build());
    }
}
