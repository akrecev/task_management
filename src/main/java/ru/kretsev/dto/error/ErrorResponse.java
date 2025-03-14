package ru.kretsev.dto.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

/**
 * Data Transfer Object representing a standardized error response.
 */
@Data
public class ErrorResponse {
    /**
     * The error message describing the issue.
     */
    private String message;

    /**
     * The HTTP status code associated with the error.
     */
    private int status;

    /**
     * The timestamp when the error occurred.
     */
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
