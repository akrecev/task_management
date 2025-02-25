package ru.kretsev.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required") String firstname,
        @NotBlank(message = "Last name is required") String lastname,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password is required")
                @Size(min = 6, message = "Password must be at least 6 characters long")
                String password,
        @Pattern(regexp = "ADMIN|USER", message = "Role must be either ADMIN or USER") String role) {
    public RegisterRequest {
        if (role == null) {
            role = "USER"; // Значение по умолчанию
        }
    }
}
