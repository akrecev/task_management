package ru.kretsev.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object for user registration request.
 *
 * @param firstname the user's first name
 * @param lastname the user's last name
 * @param email the user's email
 * @param password the user's password
 * @param role the user's role ("USER" or "ADMIN")
 */
public record RegisterRequest(
        @NotBlank(message = "First name is required") String firstname,
        @NotBlank(message = "Last name is required") String lastname,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password is required")
                @Pattern(
                        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
                        message =
                                "Password must be at least 6 characters long and must contain at least one letter and one number")
                String password,
        @Pattern(regexp = "ADMIN|USER", message = "Role must be either ADMIN or USER") String role) {}
