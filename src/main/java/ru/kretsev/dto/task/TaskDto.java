package ru.kretsev.dto.task;

import java.util.List;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.dto.user.UserShortDto;

/**
 * Data Transfer Object for a task.
 *
 * @param id the task ID
 * @param title the task title
 * @param description the task description
 * @param status the task status
 * @param priority the task priority
 * @param author the task author DTO
 * @param assignee the task assignee DTO
 * @param comments the list of comment DTOs
 */
public record TaskDto(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        UserShortDto author,
        UserShortDto assignee,
        List<CommentDto> comments) {}
