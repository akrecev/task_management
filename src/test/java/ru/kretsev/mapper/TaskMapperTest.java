package ru.kretsev.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.dto.task.TaskShortDto;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.model.comment.Comment;
import ru.kretsev.model.task.Priority;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.task.TaskStatus;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;

@SpringBootTest
class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;

    private Task task;
    private TaskDto taskDto;

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

        User assignee = User.builder()
                .id(2L)
                .firstname("Jane")
                .lastname("Smith")
                .email("jane@example.com")
                .password("securepass")
                .role(Role.ROLE_USER)
                .build();

        task = Task.builder()
                .id(10L)
                .title("Test Task")
                .description("Task description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(Priority.HIGH)
                .author(author)
                .assignee(assignee)
                .comments(List.of(new Comment(100L, "Test comment", null, author)))
                .build();

        taskDto = new TaskDto(
                10L,
                "Test Task",
                "Task description",
                TaskStatus.IN_PROGRESS.name(),
                Priority.HIGH.name(),
                new UserShortDto(1L, "John", "Doe", Role.ROLE_USER.name()),
                new UserShortDto(2L, "Jane", "Smith", Role.ROLE_USER.name()),
                List.of(new CommentDto(
                        100L,
                        "Test comment",
                        new TaskShortDto(10L, "Test Task"),
                        new UserShortDto(1L, "John", "Doe", Role.ROLE_USER.name()))));
    }

    @Test
    @DisplayName("Должен корректно маппить Task в TaskDto")
    void shouldMapTaskToTaskDto() {
        TaskDto mappedDto = taskMapper.toDto(task);

        assertNotNull(mappedDto);
        assertEquals(task.getId(), mappedDto.id());
        assertEquals(task.getTitle(), mappedDto.title());
        assertEquals(task.getDescription(), mappedDto.description());
    }

    @Test
    @DisplayName("Должен корректно маппить TaskDto в Task")
    void shouldMapTaskDtoToTask() {
        Task mappedTask = taskMapper.toEntity(taskDto);

        assertNotNull(mappedTask);
        assertEquals(taskDto.id(), mappedTask.getId());
        assertEquals(taskDto.title(), mappedTask.getTitle());
        assertEquals(taskDto.description(), mappedTask.getDescription());
    }

    @Test
    @DisplayName("Должен корректно маппить Task в TaskShortDto")
    void shouldMapTaskToTaskShortDto() {
        TaskShortDto taskShortDto = taskMapper.toShortDto(task);

        assertNotNull(taskShortDto);
        assertEquals(task.getId(), taskShortDto.id(), "ID должен совпадать");
        assertEquals(task.getTitle(), taskShortDto.title(), "Название задачи должно совпадать");
    }
}
