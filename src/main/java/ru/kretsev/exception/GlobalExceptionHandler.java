package ru.kretsev.exception;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kretsev.service.LoggingService;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final LoggingService loggingService;

    /**
     * Handles validation exceptions from invalid request data.
     *
     * @param e the validation exception
     * @return a map of field errors with messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        loggingService.logWarn("Ошибка валидации данных: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles data integrity violations, such as duplicate email.
     *
     * @param ex the data integrity exception
     * @return an error message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("users_email_key")) {
            loggingService.logWarn("Email уже используется другим пользователем: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email уже используется другим пользователем.");
        }

        loggingService.logError("Нарушение целостности данных: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Нарушение целостности данных: " + ex.getMessage());
    }

    /**
     * Handles access denied exceptions.
     *
     * @param e the access denied exception
     * @return an error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        loggingService.logWarn("Доступ запрещен: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    /**
     * Handles entity not found exceptions.
     *
     * @param e the entity not found exception
     * @return an error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        loggingService.logError("Данные отсутствуют: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * @param e the exception
     * @return an error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleAllExceptions(Exception e) {
        loggingService.logError("Ошибка выполнения программы: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Произошла внутренняя ошибка сервера \n" + e.getMessage());
    }
}
