package ru.kretsev.service;

import java.util.List;
import org.springframework.data.domain.Page;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.model.user.User;

/**
 * Service interface for managing tasks.
 */
public interface TaskService {
    /**
     * Creates a new task.
     *
     * @param taskDto the task details
     * @param user the user creating the task
     * @return the created task DTO
     */
    TaskDto createTask(TaskDto taskDto, User user);

    /**
     * Assigns a task to a user.
     *
     * @param taskId the task ID
     * @param userId the user ID
     * @return the updated task DTO
     */
    TaskDto assignTask(Long taskId, Long userId);

    /**
     * Retrieves all tasks with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @return a paginated list of task DTOs
     */
    Page<TaskDto> getAllTasks(int page, int size);

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the task ID
     * @return the task DTO
     */
    TaskDto getTask(Long taskId);

    /**
     * Retrieves tasks for a user.
     *
     * @param user the user
     * @return a list of task DTOs
     */
    List<TaskDto> getUserTasks(User user, int page, int size);

    /**
     * Updates an existing task.
     *
     * @param taskId the task ID
     * @param taskDto the updated task details
     * @param user the user updating the task
     * @return the updated task DTO
     */
    TaskDto updateTask(Long taskId, TaskDto taskDto, User user);

    /**
     * Deletes a task.
     *
     * @param taskId the task ID
     */
    void deleteTask(Long taskId);

    /**
     * Deletes a comment from a task.
     *
     * @param commentId the comment ID
     */
    void deleteComment(Long taskId, Long commentId);
}
