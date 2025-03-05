package ru.kretsev.dto.comment;

import ru.kretsev.dto.task.TaskShortDto;
import ru.kretsev.dto.user.UserShortDto;

/**
 * Data Transfer Object for a comment.
 *
 * @param id the comment ID
 * @param content the comment text
 * @param taskShortDto the short task DTO
 * @param userShortDto the short user DTO
 */
public record CommentDto(Long id, String content, TaskShortDto taskShortDto, UserShortDto userShortDto) {}
