package ru.kretsev.mapper;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("Должен корректно маппить registerRequest в user")
    void shouldMapRegisterRequestToUser() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("Firstname", "Lastname", "test@test.com", "password");

        // when
        User mappedUser = userMapper.toEntity(registerRequest);

        // then
        assertNotNull(mappedUser);
        assertEquals(registerRequest.firstname(), mappedUser.getFirstname());
        assertEquals(registerRequest.lastname(), mappedUser.getLastname());
        assertEquals(registerRequest.email(), mappedUser.getEmail());
        assertEquals(registerRequest.password(), mappedUser.getPassword());
    }

    @Test
    @DisplayName("Должен корректно маппить user в userShortDto")
    void shouldMapUserToUserShortDto() {
        // given
        User user = User.builder()
                .id(100500L)
                .firstname("Firstname")
                .lastname("Lastname")
                .email("test@test.com")
                .password("password")
                .build();

        // when
        UserShortDto userShortDto = userMapper.toShortDto(user);

        // then
        assertNotNull(userShortDto);
        assertEquals(user.getId(), userShortDto.id());
        assertEquals(user.getFirstname(), userShortDto.firstname());
        assertEquals(user.getLastname(), userShortDto.lastname());
    }
}
