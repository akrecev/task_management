package ru.kretsev.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Finds tasks by author with pagination.
     *
     * @param author the task author
     * @param pageable the pagination information
     * @return a list of tasks
     */
    Page<Task> findByAuthor(User author, Pageable pageable);
}
