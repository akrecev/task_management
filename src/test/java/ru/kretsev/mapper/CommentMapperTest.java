package ru.kretsev.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.dto.task.TaskShortDto;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.model.comment.Comment;
import ru.kretsev.model.task.Priority;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.task.TaskStatus;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;

/**
 * Unit tests for the CommentMapper.
 */
@SpringBootTest
class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        User author = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("securepass")
                .role(Role.ROLE_USER)
                .build();

        Task task = Task.builder()
                .id(10L)
                .title("Test Task")
                .description("Task description")
                .status(TaskStatus.PENDING)
                .priority(Priority.MEDIUM)
                .author(author)
                .assignee(author)
                .build();

        comment = Comment.builder()
                .id(100L)
                .content("This is a test comment")
                .task(task)
                .author(author)
                .build();

        commentDto = new CommentDto(
                100L,
                "This is a test comment",
                new TaskShortDto(10L, "Test Task"),
                new UserShortDto(1L, "John", "Doe", Role.ROLE_USER.name()));
    }

    @Test
    @DisplayName("Должен корректно маппить Comment в CommentDto")
    void shouldMapCommentToCommentDto() {
        CommentDto mappedDto = commentMapper.toDto(comment);

        assertNotNull(mappedDto, "Маппинг не должен вернуть null");
        assertEquals(comment.getId(), mappedDto.id(), "ID должен совпадать");
        assertEquals(comment.getContent(), mappedDto.content(), "Содержимое должно совпадать");
        assertEquals(comment.getTask().getId(), mappedDto.taskShortDto().id(), "ID задачи должен совпадать");
        assertEquals(
                comment.getTask().getTitle(), mappedDto.taskShortDto().title(), "Название задачи должно совпадать");
        assertEquals(comment.getAuthor().getId(), mappedDto.userShortDto().id(), "ID автора должен совпадать");
        assertEquals(
                comment.getAuthor().getFirstname(),
                mappedDto.userShortDto().firstname(),
                "Имя автора должно совпадать");
        assertEquals(
                comment.getAuthor().getLastname(),
                mappedDto.userShortDto().lastname(),
                "Фамилия автора должна совпадать");
    }

    @Test
    @DisplayName("Должен корректно маппить CommentDto в Comment")
    void shouldMapCommentDtoToComment() {
        Comment mappedComment = commentMapper.toEntity(commentDto);

        assertNotNull(mappedComment, "Маппинг не должен вернуть null");
        assertEquals(commentDto.id(), mappedComment.getId(), "ID должен совпадать");
        assertEquals(commentDto.content(), mappedComment.getContent(), "Содержимое должно совпадать");

        assertNotNull(mappedComment.getTask(), "Task не должен быть null");
        assertEquals(commentDto.taskShortDto().id(), mappedComment.getTask().getId(), "ID задачи должен совпадать");

        assertNotNull(mappedComment.getAuthor(), "Author не должен быть null");
        assertEquals(commentDto.userShortDto().id(), mappedComment.getAuthor().getId(), "ID автора должен совпадать");
    }
}
