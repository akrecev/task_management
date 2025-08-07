package ru.kretsev.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for user authentication request.
 *
 * @param email the user's email
 * @param password the user's password
 */
public record AuthenticationRequest(
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password is required")
                @Size(min = 6, message = "Password must be at least 6 characters long")
                String password) {}
