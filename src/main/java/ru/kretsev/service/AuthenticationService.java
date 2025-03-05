package ru.kretsev.service;

import ru.kretsev.dto.user.AuthenticationRequest;
import ru.kretsev.dto.user.AuthenticationResponse;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.dto.user.UserShortDto;

/**
 * Service interface for user authentication and registration.
 */
public interface AuthenticationService {

    /**
     * Registers a new user.
     *
     * @param request the registration request
     * @return the authentication response with a JWT token
     */
    AuthenticationResponse register(RegisterRequest request);

    /**
     * Authenticates a user.
     *
     * @param request the authentication request
     * @return the authentication response with a JWT token
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Retrieves a user by username (email).
     *
     * @param username the user's email
     * @return the short user DTO
     */
    UserShortDto getUserByUsername(String username);
}
