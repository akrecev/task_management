package ru.kretsev.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.model.user.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private RegisterRequest registerRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("Firstname", "Lastname", "test@test.com", "password");
        user = User.builder()
                .id(100500L)
                .firstname("Firstname")
                .lastname("Lastname")
                .email("test@test.com")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("User mapping: registerRequest -> user")
    void shouldMapRegisterRequestToUser() {
        // when
        User user = userMapper.toEntity(registerRequest);

        // then
        assertNotNull(user);
        assertEquals(registerRequest.firstname(), user.getFirstname());
        assertEquals(registerRequest.lastname(), user.getLastname());
        assertEquals(registerRequest.email(), user.getEmail());
        assertEquals(registerRequest.password(), user.getPassword());
    }

    @Test
    @DisplayName("User mapping: user -> userShortDto")
    void shouldMapUserToUserShortDto() {
        // when
        UserShortDto userShortDto = userMapper.toShortDto(user);

        // then
        assertNotNull(userShortDto);
        assertEquals(user.getId(), userShortDto.id());
        assertEquals(user.getFirstname(), userShortDto.firstname());
        assertEquals(user.getLastname(), userShortDto.lastname());
    }
}
