package edu.miu.cs544.reactivecontentplatform.controller.admin;

import edu.miu.cs544.reactivecontentplatform.model.Category;
import edu.miu.cs544.reactivecontentplatform.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Category>>> getAll() {
        return categoryService.findAll()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Category>> getById(@PathVariable String id) {
        return categoryService.findById(id)
                .map(opt -> opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> create(@RequestBody Category category) {
        return categoryService.create(category)
                .map(saved -> ResponseEntity.created(URI.create("/api/admin/categories/" + saved.getId())).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Category>> update(@PathVariable String id, @RequestBody Category category) {
        return categoryService.update(id, category)
                .map(opt -> opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return categoryService.delete(id)
                .map(deleted -> deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build());
    }
}
