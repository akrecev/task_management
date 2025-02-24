package ru.kretsev.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kretsev.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
