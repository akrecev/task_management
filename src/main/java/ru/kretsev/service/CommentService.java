package ru.kretsev.service;

import org.springframework.data.domain.Page;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.user.User;

/**
 * Service interface for managing comments.
 */
public interface CommentService {

    /**
     * Retrieves a comment by its ID.
     *
     * @param commentId the comment ID
     * @return the comment DTO
     */
    CommentDto getCommentById(Long commentId);

    /**
     * Retrieves comments for a task with pagination.
     *
     * @param taskId the task ID
     * @param page the page number
     * @param size the page size
     * @return a paginated list of comment DTOs
     */
    Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int size);

    /**
     * Adds a new comment to a task.
     *
     * @param taskId the task ID
     * @param commentDto the comment details
     * @param user the user adding the comment
     * @return the created comment DTO
     */
    CommentDto addComment(Long taskId, CommentDto commentDto, User user);

    /**
     * Deletes a comment.
     *
     * @param commentId the comment ID
     */
    void deleteComment(Long commentId);
}
