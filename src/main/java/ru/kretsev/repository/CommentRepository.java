package ru.kretsev.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kretsev.model.comment.Comment;

/**
 * Repository interface for Comment entity operations.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds comments by task ID with pagination.
     *
     * @param taskId the task ID
     * @param pageable the pagination information
     * @return a page of comments
     */
    Page<Comment> findByTaskId(Long taskId, Pageable pageable);
}
