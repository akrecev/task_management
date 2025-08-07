package ru.kretsev.exception;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kretsev.dto.error.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String errorMessage = "Validation failed: " + errors;

        loggingService.logWarn("Ошибка валидации данных: {}", errors);
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Handles data integrity violations, such as duplicate email.
     *
     * @param e the data integrity exception
     * @return an error message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if (e.getMessage() != null && e.getMessage().contains("users_email_key")) {
            loggingService.logWarn("Email уже используется другим пользователем: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            "Email уже используется другим пользователем.", HttpStatus.CONFLICT.value()));
        }

        loggingService.logError("Нарушение целостности данных: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Нарушение целостности данных", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * Handles access denied exceptions.
     *
     * @param e the access denied exception
     * @return an error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        loggingService.logWarn("Доступ запрещён: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Доступ запрещён: " + e.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    /**
     * Handles entity not found exceptions.
     *
     * @param e the entity not found exception
     * @return an error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        loggingService.logWarn("Сущность не найдена: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    /**
     * Handles illegal arguments exception.
     *
     * @param e the entity not found exception
     * @return an error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        loggingService.logWarn("Некорректный аргумент: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Некорректный аргумент: " + e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Handles authentication exceptions, such as invalid or missing tokens.
     *
     * @param ex the AuthenticationException thrown during authentication
     * @return ResponseEntity containing an ErrorResponse with the error details
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        loggingService.logWarn("Ошибка аутентификации: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Неверный или отсутствующий токен", HttpStatus.UNAUTHORIZED.value()));
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * @param e the exception
     * @return an error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        loggingService.logError("Непредвиденная ошибка: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
