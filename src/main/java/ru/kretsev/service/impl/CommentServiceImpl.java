package ru.kretsev.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.kretsev.auth.AuthenticationFacade;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.mapper.CommentMapper;
import ru.kretsev.model.comment.Comment;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.CommentRepository;
import ru.kretsev.repository.TaskRepository;
import ru.kretsev.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByTaskId(taskId, pageable);
        return commentsPage.map(commentMapper::toDto);
    }

    @Override
    public CommentDto addComment(Long taskId, CommentDto commentDto, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Задача не найдена"));

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setTask(task);
        comment.setAuthor(user);
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        var comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден"));

        String currentUserEmail = authenticationFacade.getCurrentUserEmail();
        List<String> currentUserRoles = authenticationFacade.getCurrentUserRoles();

        boolean isAdmin = currentUserRoles.contains("ROLE_ADMIN");
        boolean isAuthor = comment.getAuthor().getEmail().equals(currentUserEmail);

        if (isAdmin || isAuthor) {
            commentRepository.delete(comment);
        } else {
            throw new AccessDeniedException("Вы не можете удалить этот комментарий");
        }
    }
}
