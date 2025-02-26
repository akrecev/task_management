package ru.kretsev.service;

import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.user.User;

public interface CommentService {
    CommentDto addComment(Long taskId, CommentDto commentDto, User user);

    void deleteComment(Long commentId);
}
