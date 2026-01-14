package w1d2springbootrestorm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import w1d2springbootrestorm.entitie.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

