package ru.kretsev.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static ru.kretsev.model.user.Role.ROLE_ADMIN;
import static ru.kretsev.model.user.Role.ROLE_USER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.model.user.User;

/**
 * Unit tests for the UserMapper.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Должен корректно маппить registerRequest в user")
    void shouldMapRegisterRequestToUser() {
        RegisterRequest registerRequest =
                new RegisterRequest("Firstname", "Lastname", "test@test.com", "password", ROLE_USER.name());

        User mappedUser = userMapper.toEntity(registerRequest);

        assertNotNull(mappedUser);
        assertEquals(registerRequest.firstname(), mappedUser.getFirstname());
        assertEquals(registerRequest.lastname(), mappedUser.getLastname());
        assertEquals(registerRequest.email(), mappedUser.getEmail());
        assertEquals(registerRequest.password(), mappedUser.getPassword());
        assertEquals(ROLE_USER, mappedUser.getRole());
    }

    @Test
    @DisplayName("Должен корректно маппить registerRequest с ролью ADMIN в user")
    void shouldMapRegisterRequestWithAdminRoleToUser() {
        RegisterRequest registerRequest =
                new RegisterRequest("Admin", "Admin", "admin@example.com", "admin123", ROLE_ADMIN.name());

        User mappedUser = userMapper.toEntity(registerRequest);

        assertNotNull(mappedUser);
        assertEquals(registerRequest.firstname(), mappedUser.getFirstname());
        assertEquals(registerRequest.lastname(), mappedUser.getLastname());
        assertEquals(registerRequest.email(), mappedUser.getEmail());
        assertEquals(registerRequest.password(), mappedUser.getPassword());
        assertEquals(ROLE_ADMIN, mappedUser.getRole());
    }

    @Test
    @DisplayName("Должен корректно маппить user в userShortDto")
    void shouldMapUserToUserShortDto() {
        User user = User.builder()
                .id(100500L)
                .firstname("Firstname")
                .lastname("Lastname")
                .email("test@test.com")
                .password("password")
                .build();

        UserShortDto userShortDto = userMapper.toShortDto(user);

        assertNotNull(userShortDto);
        assertEquals(user.getId(), userShortDto.id());
        assertEquals(user.getFirstname(), userShortDto.firstname());
        assertEquals(user.getLastname(), userShortDto.lastname());
    }
}
