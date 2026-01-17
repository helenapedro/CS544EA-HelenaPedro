package edu.miu.cs544.reactivecontentplatform.service;

import edu.miu.cs544.reactivecontentplatform.model.Blog;
import edu.miu.cs544.reactivecontentplatform.model.Category;
import edu.miu.cs544.reactivecontentplatform.repository.blocking.BlogRepository;
import edu.miu.cs544.reactivecontentplatform.repository.blocking.CategoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;

    public BlogService(BlogRepository blogRepository, CategoryRepository categoryRepository) {
        this.blogRepository = blogRepository;
        this.categoryRepository = categoryRepository;
    }

    public Mono<List<Blog>> findAll() {
        return Mono.fromCallable(blogRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(reactor.core.publisher.Flux::fromIterable)
                .flatMap(this::enrichWithCategory)
                .collectList();
    }

    public Mono<Optional<Blog>> findById(String id) {
        return Mono.fromCallable(() -> blogRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optBlog ->
                        optBlog.map(blog ->
                                        enrichWithCategory(blog).map(Optional::of)
                                )
                                .orElseGet(() -> Mono.just(Optional.empty()))
                );
    }

    public Mono<Blog> create(Blog blog) {
        blog.setId(null);
        return Mono.fromCallable(() -> {
                    if (blog.getCategoryId() != null && !categoryRepository.existsById(blog.getCategoryId())) {
                        throw new IllegalArgumentException("Category not found: " + blog.getCategoryId());
                    }
                    return blogRepository.save(blog);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(this::enrichWithCategory);
    }

    public Mono<Optional<Blog>> update(String id, Blog blog) {
        return Mono.fromCallable(() -> blogRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optExisting -> {
                    if (optExisting.isEmpty()) return Mono.just(Optional.empty());
                    Blog existing = optExisting.get();

                    return Mono.fromCallable(() -> {
                                existing.setTitle(blog.getTitle());
                                existing.setAuthor(blog.getAuthor());
                                existing.setCategoryId(blog.getCategoryId());

                                if (existing.getCategoryId() != null && !categoryRepository.existsById(existing.getCategoryId())) {
                                    throw new IllegalArgumentException("Category not found: " + existing.getCategoryId());
                                }
                                return blogRepository.save(existing);
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(this::enrichWithCategory)
                            .map(Optional::of);
                });
    }
    
    public Mono<Boolean> delete(String id) {
        return Mono.fromCallable(() -> {
                    if (!blogRepository.existsById(id)) return false;
                    blogRepository.deleteById(id);
                    return true;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Blog> enrichWithCategory(Blog blog) {
        if (blog.getCategoryId() == null) {
            blog.setCategory(null);
            return Mono.just(blog);
        }

        return Mono.fromCallable(() -> categoryRepository.findById(blog.getCategoryId()))
                .subscribeOn(Schedulers.boundedElastic())
                .map(optCat -> {
                    blog.setCategory(optCat.orElse(null));
                    return blog;
                });
    }
}
