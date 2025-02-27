package ru.kretsev.service;

import org.springframework.data.domain.Page;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.user.User;

public interface CommentService {
    CommentDto getCommentById(Long commentId);

    Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int size);

    CommentDto addComment(Long taskId, CommentDto commentDto, User user);

    void deleteComment(Long commentId);
}
