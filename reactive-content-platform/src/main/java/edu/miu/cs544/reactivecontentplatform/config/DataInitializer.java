package edu.miu.cs544.reactivecontentplatform.config;

import edu.miu.cs544.reactivecontentplatform.model.Blog;
import edu.miu.cs544.reactivecontentplatform.model.Category;
import edu.miu.cs544.reactivecontentplatform.model.Post;
import edu.miu.cs544.reactivecontentplatform.repository.blocking.BlogRepository;
import edu.miu.cs544.reactivecontentplatform.repository.blocking.CategoryRepository;
import edu.miu.cs544.reactivecontentplatform.repository.reactive.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                           BlogRepository blogRepository,
                           PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.blogRepository = blogRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {
        postRepository.deleteAll().block();
        blogRepository.deleteAll();
        categoryRepository.deleteAll();

        Category engineering = categoryRepository.save(new Category(null, "Engineering"));
        Category lifestyle = categoryRepository.save(new Category(null, "LifeStyle"));

        Blog tech = blogRepository.save(new Blog(null, "Tech Chronicles", "Admin", engineering.getId(), null));
        Blog daily = blogRepository.save(new Blog(null, "Daily Notes", "Admin", lifestyle.getId(), null));

        List<Post> seedPosts = List.of(
                new Post(null, tech.getId(), "Welcome to Tech Chronicles!", Instant.now().minusSeconds(3600)),
                new Post(null, tech.getId(), "Reactive streams are awesome.", Instant.now().minusSeconds(1800)),
                new Post(null, daily.getId(), "First post in Daily Notes.", Instant.now().minusSeconds(900))
        );

        postRepository.saveAll(seedPosts).collectList().block();

        System.out.println("[SEED] Initialized sample data:");
        System.out.println("[SEED] Categories: " + categoryRepository.count());
        System.out.println("[SEED] Blogs: " + blogRepository.count());
        System.out.println("[SEED] Posts: " + postRepository.count().block());
    }
}
