package ru.kretsev.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.kretsev.service.EntityService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final EntityService entityService;

    @Override
    public Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByTaskId(taskId, pageable);
        return commentsPage.map(commentMapper::toDto);
    }

    @Override
    public CommentDto addComment(Long taskId, CommentDto commentDto, User user) {
        log.info("Попытка добавления комментария к задаче: taskId={}, user={}", taskId, user.getEmail());

        Task task = entityService.findEntityOrElseThrow(taskRepository, taskId, "Задача не найдена");

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setTask(task);
        comment.setAuthor(user);
        commentRepository.save(comment);

        log.info("Комментарий успешно добавлен: id={}, taskId={}", comment.getId(), taskId);
        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        log.info("Попытка удаления комментария: id={}", commentId);

        Comment comment = entityService.findEntityOrElseThrow(commentRepository, commentId, "Комментарий не найден");

        String currentUserEmail = authenticationFacade.getCurrentUserEmail();
        List<String> currentUserRoles = authenticationFacade.getCurrentUserRoles();

        boolean isAdmin = currentUserRoles.contains("ROLE_ADMIN");
        boolean isAuthor = comment.getAuthor().getEmail().equals(currentUserEmail);

        if (isAdmin || isAuthor) {
            commentRepository.delete(comment);
            log.info("Комментарий успешно удален: id={}", commentId);
        } else {
            log.warn("Попытка удаления комментария без прав: commentId={}, user={}", commentId, currentUserEmail);
            throw new AccessDeniedException("Вы не можете удалить этот комментарий");
        }
    }
}
