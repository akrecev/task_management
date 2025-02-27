package ru.kretsev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.model.user.User;
import ru.kretsev.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Методы для работы с задачами")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Создать задачу")
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTask(taskDto, user));
    }

    @Operation(summary = "Назначить задачу пользователю (только администратор)")
    @PutMapping("/{taskId}/assign/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TaskDto> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTask(taskId, userId));
    }

    @Operation(summary = "Получить все задачи (только администратор, с поддержкой пагинации)")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<TaskDto> tasks = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Получить одну задачу по ID")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        TaskDto task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Получить список задач текущего пользователя")
    @GetMapping
    public ResponseEntity<List<TaskDto>> getUserTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getUserTasks(user));
    }

    @Operation(summary = "Обновить задачу")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId, @RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDto, user));
    }

    @Operation(summary = "Удалить задачу (только администратор или владелец)")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удаление комментария (только администратор или владелец)")
    @DeleteMapping("/{taskId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long taskId, @PathVariable Long commentId) {
        taskService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
