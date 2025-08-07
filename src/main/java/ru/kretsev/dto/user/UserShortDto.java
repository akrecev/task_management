package ru.kretsev.dto.user;

/**
 * Short Data Transfer Object for a user.
 *
 * @param id the user ID
 * @param firstname the user's first name
 * @param lastname the user's last name
 * @param role the user's role
 */
public record UserShortDto(Long id, String firstname, String lastname, String role) {}
