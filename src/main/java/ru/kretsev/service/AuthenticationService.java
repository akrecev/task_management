package ru.kretsev.service;

import ru.kretsev.dto.user.AuthenticationRequest;
import ru.kretsev.dto.user.AuthenticationResponse;
import ru.kretsev.dto.user.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
