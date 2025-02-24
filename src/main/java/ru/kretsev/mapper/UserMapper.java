package ru.kretsev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.kretsev.dto.user.RegisterRequest;
import ru.kretsev.model.user.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(RegisterRequest registerRequest);
}
