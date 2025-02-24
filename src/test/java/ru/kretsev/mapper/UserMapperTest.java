package ru.kretsev.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.model.user.User;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("User mapping: registerRequest -> user")
    void shouldMapRegisterRequestToUser() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("Firstname", "Lastname", "test@test.com", "password");

        // when
        User user = userMapper.toEntity(registerRequest);

        // then
        assertNotNull(user);
        assertEquals(registerRequest.firstname(), user.getFirstname());
        assertEquals(registerRequest.lastname(), user.getLastname());
        assertEquals(registerRequest.email(), user.getEmail());
        assertEquals(registerRequest.password(), user.getPassword());
    }
}
