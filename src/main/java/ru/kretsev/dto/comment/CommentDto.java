package ru.kretsev.dto.comment;

import ru.kretsev.dto.task.TaskShortDto;
import ru.kretsev.dto.user.UserShortDto;

public record CommentDto(Long id, String content, TaskShortDto taskShortDto, UserShortDto userShortDto) {}
