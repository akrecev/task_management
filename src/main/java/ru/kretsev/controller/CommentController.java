package ru.kretsev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.user.User;
import ru.kretsev.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Добавить комментарий к задаче")
    @PostMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long taskId, @RequestBody @Valid CommentDto commentDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(commentService.addComment(taskId, commentDto, user));
    }

    @Operation(summary = "Удалить комментарий (только если это свой комментарий или админ)")
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
