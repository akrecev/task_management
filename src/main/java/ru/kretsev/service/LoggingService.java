package ru.kretsev.service;

/**
 * Service interface for logging messages at various severity levels.
 * Provides methods to log debug, info, warning, and error messages with support for parameterized formatting.
 */
public interface LoggingService {
    /**
     * Logs a debug-level message, typically used for detailed diagnostic information during development.
     * This level is usually disabled in production environments.
     *
     * @param message the message template with placeholders (e.g., "Processing item {}", "User {} logged in")
     * @param args    the arguments to substitute into the message placeholders
     */
    void logDebug(String message, Object... args);

    /**
     * Logs an info-level message, used for general operational information about the application's progress.
     * Examples include startup messages, successful completions, or significant state changes.
     *
     * @param message the message template with placeholders (e.g., "Application started on port {}", "Task {} completed")
     * @param args    the arguments to substitute into the message placeholders
     */
    void logInfo(String message, Object... args);

    /**
     * Logs a warning-level message, indicating a potential problem or unexpected situation that does not
     * prevent the application from functioning.
     *
     * @param message the message template with placeholders (e.g., "Resource {} is low", "Unexpected value {}")
     * @param args    the arguments to substitute into the message placeholders
     */
    void logWarn(String message, Object... args);

    /**
     * Logs an error-level message, used for reporting serious issues or exceptions that may impact
     * the application's functionality or reliability.
     *
     * @param message the message template with placeholders (e.g., "Failed to process request {}", "Error: {}")
     * @param args    the arguments to substitute into the message placeholders
     */
    void logError(String message, Object... args);
}
