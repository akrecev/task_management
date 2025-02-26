package ru.kretsev.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kretsev.dto.user.AuthenticationRequest;
import ru.kretsev.dto.user.AuthenticationResponse;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.UserRepository;
import ru.kretsev.service.impl.AuthenticationServiceImpl;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private static final String MOCK_JWT_TOKEN = "mocked-jwt-token";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    private User createTestUser() {
        return User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .build();
    }

    @Test
    @DisplayName("Регистрация нового пользователя - успешный сценарий")
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("John", "Doe", EMAIL, PASSWORD, "USER");
        User user = createTestUser();

        when(passwordEncoder.encode(request.password())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(MOCK_JWT_TOKEN);

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals(MOCK_JWT_TOKEN, response.token());
        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("USER", savedUser.getRole().name());
    }

    @Test
    @DisplayName("Аутентификация пользователя - успешный вход")
    void testAuthenticate_Success() {
        AuthenticationRequest request = new AuthenticationRequest(EMAIL, PASSWORD);
        User user = createTestUser();
        user.setRole(Role.USER);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(MOCK_JWT_TOKEN);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, PASSWORD));

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals(MOCK_JWT_TOKEN, response.token());

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
    }

    @Test
    @DisplayName("Регистрация администратора - успешный сценарий")
    void testRegisterAdmin_Success() {
        RegisterRequest request = new RegisterRequest("Admin", "Admin", "admin@example.com", "admin123", "ADMIN");
        User admin = User.builder()
                .id(2L)
                .firstname("Admin")
                .lastname("Admin")
                .email("admin@example.com")
                .password(ENCODED_PASSWORD)
                .role(Role.ADMIN)
                .build();

        when(passwordEncoder.encode(request.password())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(admin);
        when(jwtService.generateToken(any(User.class))).thenReturn(MOCK_JWT_TOKEN);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin@example.com", // principal
                "admin123", // credentials
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))); // authorities
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals(MOCK_JWT_TOKEN, response.token());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(Role.ADMIN, savedUser.getRole());

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Аутентификация - ошибка при неверном пароле")
    void testAuthenticate_Failed_WrongPassword() {
        AuthenticationRequest request = new AuthenticationRequest(EMAIL, "wrongpassword");
        User user = createTestUser();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        Exception exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
