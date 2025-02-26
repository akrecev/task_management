package ru.kretsev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kretsev.dto.user.AuthenticationRequest;
import ru.kretsev.dto.user.AuthenticationResponse;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Методы для регистрации и входа в систему")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация нового пользователя", description = "Создаёт нового пользователя в системе")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
            })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Аутентификация пользователя", description = "Возвращает JWT-токен при успешной авторизации")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
                @ApiResponse(responseCode = "403", description = "Неверные учетные данные")
            })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Получение информации о текущем пользователе")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserShortDto> getCurrentUser(Authentication authentication) {
        UserShortDto userDto = authenticationService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(userDto);
    }
}
