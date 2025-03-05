package ru.kretsev.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.user.User;

/**
 * Repository interface for Task entity operations.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds tasks by author.
     *
     * @param author the task author
     * @return a list of tasks
     */
    List<Task> findByAuthor(User author);
}
