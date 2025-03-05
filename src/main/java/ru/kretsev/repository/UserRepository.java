package ru.kretsev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email.
     *
     * @param email the user's email
     * @return an optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds users by role.
     *
     * @param role the user role
     * @return a list of users
     */
    List<User> findByRole(Role role);
}
