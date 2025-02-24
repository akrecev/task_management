package ru.kretsev.dto.task;

import java.util.List;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.dto.user.UserShortDto;

public record TaskDto(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        UserShortDto author,
        UserShortDto assignee,
        List<CommentDto> comments) {}
