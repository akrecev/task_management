package ru.kretsev.dto.user;

/**
 * Data Transfer Object for authentication response.
 *
 * @param token the JWT token
 */
public record AuthenticationResponse(String token) {}
