package edu.miu.cs544.reactivecontentplatform.service;

import edu.miu.cs544.reactivecontentplatform.model.Category;
import edu.miu.cs544.reactivecontentplatform.repository.blocking.CategoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<List<Category>> findAll() {
        return Mono.fromCallable(categoryRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Optional<Category>> findById(String id) {
        return Mono.fromCallable(() -> categoryRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Category> create(Category category) {
        category.setId(null);
        return Mono.fromCallable(() -> categoryRepository.save(category))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Optional<Category>> update(String id, Category category) {
        return Mono.fromCallable(() -> {
            return categoryRepository.findById(id).map(existing -> {
                existing.setName(category.getName());
                return categoryRepository.save(existing);
            });
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> delete(String id) {
        return Mono.fromCallable(() -> {
            if (!categoryRepository.existsById(id)) return false;
            categoryRepository.deleteById(id);
            return true;
        })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
