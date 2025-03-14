package ru.kretsev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kretsev.auth.JwtService;
import ru.kretsev.dto.user.AuthenticationRequest;
import ru.kretsev.dto.user.AuthenticationResponse;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.mapper.UserMapper;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.UserRepository;
import ru.kretsev.service.AuthenticationService;
import ru.kretsev.service.LoggingService;

/**
 * Implementation of the AuthenticationService for user registration and authentication.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoggingService loggingService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        loggingService.logInfo("Попытка регистрации пользователя: email={}", request.email());

        var role = Role.ROLE_USER;

        if (request.role() != null && request.role().equals(Role.ROLE_ADMIN.name())) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                    && authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                role = Role.ROLE_ADMIN;
            }
        }

        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        loggingService.logInfo("Пользователь успешно зарегистрирован: email={}", request.email());
        return new AuthenticationResponse(jwtToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        loggingService.logInfo("Попытка аутентификации пользователя: email={}", request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var user = userRepository.findByEmail(request.email()).orElseThrow(() -> {
            loggingService.logError("Пользователь с email={} не найден", request.email());
            return new UsernameNotFoundException("User not found");
        });

        var jwtToken = jwtService.generateToken(user);

        loggingService.logInfo("Пользователь успешно аутентифицирован: email={}", request.email());
        return new AuthenticationResponse(jwtToken);
    }

    @Override
    public UserShortDto getUserByUsername(String username) {
        var user =
                userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userMapper.toShortDto(user);
    }
}
