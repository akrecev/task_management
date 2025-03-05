package ru.kretsev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.user.User;
import ru.kretsev.service.CommentService;

/**
 * REST controller for managing comments in the Task Management System.
 */
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
public class CommentController {
    private final CommentService commentService;

    /**
     * Adds a new comment to a task.
     *
     * @param taskId the ID of the task
     * @param commentDto the comment details
     * @param user the authenticated user
     * @return the created comment DTO
     */
    @Operation(summary = "Добавить комментарий к задаче")
    @PostMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long taskId, @RequestBody @Valid CommentDto commentDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(commentService.addComment(taskId, commentDto, user));
    }

    /**
     * Retrieves a comment by its ID (admin only).
     *
     * @param commentId the ID of the comment
     * @return the comment DTO
     */
    @Operation(summary = "Получить комментарий по id")
    @GetMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentDto> getCommentsByTaskId(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    /**
     * Retrieves all comments for a task with pagination.
     *
     * @param taskId the ID of the task
     * @param page the page number (default 0)
     * @param size the page size (default 10)
     * @return a paginated list of comment DTOs
     */
    @Operation(summary = "Получить все комментарии к задаче (с пагинацией)")
    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<CommentDto>> getCommentsByTaskId(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, page, size));
    }

    /**
     * Deletes a comment if the user is the author or an admin.
     *
     * @param commentId the ID of the comment
     * @return no content response
     */
    @Operation(summary = "Удалить комментарий (только если это свой комментарий или админ)")
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
