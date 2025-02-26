package ru.kretsev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Создать задачу")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTask(taskDto, user));
    }

    @Operation(summary = "Назначить задачу пользователю (только администратор)")
    @PutMapping("/{taskId}/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTask(taskId, userId));
    }

    @Operation(summary = "Получить список задач текущего пользователя")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskDto>> getUserTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getUserTasks(user));
    }

    @Operation(summary = "Обновить задачу")
    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId, @RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDto, user));
    }

    @Operation(summary = "Удалить задачу (только администратор)")
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
