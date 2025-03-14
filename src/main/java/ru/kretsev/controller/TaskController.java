package ru.kretsev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.model.user.User;
import ru.kretsev.service.TaskService;

/**
 * REST controller for managing tasks in the Task Management System.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Методы для работы с задачами")
public class TaskController {
    private final TaskService taskService;

    /**
     * Creates a new task and assigns it to the authenticated user as the author.
     *
     * @param taskDto the task details DTO
     * @param user    the authenticated user creating the task
     * @return ResponseEntity containing the created TaskDto
     */
    @Operation(summary = "Создать задачу")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных задачи"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
            })
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTask(taskDto, user));
    }

    /**
     * Assigns a task to a specified user (admin only).
     *
     * @param taskId the ID of the task to assign
     * @param userId the ID of the user to assign the task to
     * @return ResponseEntity containing the updated TaskDto
     */
    @Operation(summary = "Назначить задачу пользователю (только администратор)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно назначена"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не администратор)"),
                    @ApiResponse(responseCode = "404", description = "Задача или пользователь не найдены")
            })
    @PutMapping("/{taskId}/assign/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TaskDto> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTask(taskId, userId));
    }

    /**
     * Retrieves all tasks with pagination (admin only).
     *
     * @param page the page number (default 0)
     * @param size the page size (default 10)
     * @return ResponseEntity containing a paginated list of TaskDtos
     */
    @Operation(summary = "Получить все задачи (только администратор, с поддержкой пагинации)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список задач успешно получен"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не администратор)")
            })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<TaskDto> tasks = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Retrieves a single task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return ResponseEntity containing the TaskDto
     */
    @Operation(summary = "Получить одну задачу по ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно получена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        TaskDto task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }

    /**
     * Retrieves the tasks of the current user with pagination.
     *
     * @param page the page number (default 0)
     * @param size the page size (default 10)
     *
     * @param user the authenticated user
     * @return a list of task DTOs
     */
    @Operation(summary = "Получить список задач текущего пользователя")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список задач пользователя успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
            })
    @GetMapping
    public ResponseEntity<List<TaskDto>> getUserTasks(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getUserTasks(user, page, size));
    }

    /**
     * Updates an existing task.
     *
     * @param taskId the ID of the task
     * @param taskDto the updated task details
     * @param user the authenticated user
     * @return the updated task DTO
     */
    @Operation(summary = "Обновить задачу")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных задачи"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не владелец задачи)"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId, @RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDto, user));
    }

    /**
     * Deletes a task (admin or owner only).
     *
     * @param taskId the ID of the task
     * @return no content response
     */
    @Operation(summary = "Удалить задачу (только администратор или владелец)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не владелец и не администратор)"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a comment from a task (admin or owner only).
     *
     * @param taskId the ID of the task
     * @param commentId the ID of the comment
     * @return no content response
     */
    @Operation(summary = "Удаление комментария (только администратор или владелец)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Комментарий успешно удалён"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён (не владелец и не администратор)"),
                    @ApiResponse(responseCode = "404", description = "Комментарий или задача не найдены")
            })
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long taskId, @PathVariable Long commentId) {
        taskService.deleteComment(taskId, commentId);
        return ResponseEntity.noContent().build();
    }
}
