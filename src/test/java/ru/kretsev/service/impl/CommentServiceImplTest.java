package ru.kretsev.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kretsev.auth.AuthenticationFacade;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.mapper.CommentMapper;
import ru.kretsev.model.comment.Comment;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.CommentRepository;
import ru.kretsev.repository.TaskRepository;
import ru.kretsev.service.EntityService;

/**
 * Unit tests for the CommentServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private EntityService entityService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void addCommentShouldReturnCommentDto() {
        Long taskId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Комментарий", null, null);
        User user = new User();
        user.setEmail("user@example.com");

        Task task = new Task();
        task.setId(taskId);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Комментарий");

        when(entityService.findEntityOrElseThrow(taskRepository, taskId, "Задача не найдена"))
                .thenReturn(task);

        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.addComment(taskId, commentDto, user);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(commentDto.content(), result.content(), "Содержание комментария должно совпадать");
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void deleteComment_ShouldDeleteComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent("Комментарий");

        User user = new User();
        user.setEmail("user@example.com");
        comment.setAuthor(user);

        when(entityService.findEntityOrElseThrow(commentRepository, commentId, "Комментарий не найден"))
                .thenReturn(comment);
        when(authenticationFacade.getCurrentUserEmail()).thenReturn("user@example.com");
        when(authenticationFacade.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).delete(comment);
    }
}
