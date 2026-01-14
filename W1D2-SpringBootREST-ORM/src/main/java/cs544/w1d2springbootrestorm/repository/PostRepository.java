package w1d2springbootrestorm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import w1d2springbootrestorm.entitie.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthor(String author);

    List<Post> findByAuthorContainingIgnoreCase(String text);
}
