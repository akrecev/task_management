package ru.kretsev.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.mapper.TaskMapper;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.TaskRepository;
import ru.kretsev.repository.UserRepository;
import ru.kretsev.service.EntityService;

/**
 * Unit tests for the TaskServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private EntityService entityService;

    @Mock
    private LoggingServiceImpl loggingService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTaskShouldReturnTaskDto() {
        TaskDto taskDto = new TaskDto(1L, "Новая задача", "Описание", "PENDING", "HIGH", null, null, List.of());
        User user = new User();
        user.setEmail("user@example.com");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Новая задача");

        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(taskDto, user);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(taskDto.title(), result.title(), "Название задачи должно совпадать");
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void assignTaskShouldAssignTaskToUser() {
        Long taskId = 1L;
        Long userId = 2L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Новая задача");

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(entityService.findEntityOrElseThrow(taskRepository, taskId, "Задача не найдена"))
                .thenReturn(task);
        when(entityService.findEntityOrElseThrow(userRepository, userId, "Пользователь не найден"))
                .thenReturn(user);

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task))
                .thenReturn(new TaskDto(taskId, "Новая задача", "Описание", "PENDING", "HIGH", null, null, List.of()));

        TaskDto result = taskService.assignTask(taskId, userId);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(userId, task.getAssignee().getId(), "ID исполнителя должно совпадать");
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getTaskShouldReturnTaskDto() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Новая задача");

        TaskDto taskDto = new TaskDto(taskId, "Новая задача", "Описание", "PENDING", "HIGH", null, null, List.of());

        when(entityService.findEntityOrElseThrow(taskRepository, taskId, "Задача не найдена"))
                .thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.getTask(taskId);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(taskId, result.id(), "ID задачи должно совпадать");
        assertEquals("Новая задача", result.title(), "Название задачи должно совпадать");
    }

    @Test
    void updateTask_ShouldThrowAccessDeniedException() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Новая задача", "Описание", "PENDING", "HIGH", null, null, List.of());
        User user =
                User.builder().email("user@example.com").role(Role.ROLE_USER).build();

        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(new User().toBuilder().email("user2@example.com").build());

        when(entityService.findEntityOrElseThrow(taskRepository, taskId, "Задача не найдена"))
                .thenReturn(task);

        assertThrows(
                AccessDeniedException.class,
                () -> taskService.updateTask(taskId, taskDto, user),
                "Должно быть выброшено исключение AccessDeniedException");
    }
}
