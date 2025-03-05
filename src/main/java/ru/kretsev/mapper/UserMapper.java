package ru.kretsev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.dto.user.UserShortDto;
import ru.kretsev.model.user.User;

/**
 * Mapper interface for converting between User entities and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Converts a RegisterRequest DTO to a User entity.
     *
     * @param registerRequest the registration request DTO
     * @return the user entity
     */
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    User toEntity(RegisterRequest registerRequest);

    /**
     * Converts a User entity to a short User DTO.
     *
     * @param user the user entity
     * @return the short user DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "role", source = "role")
    UserShortDto toShortDto(User user);
}
