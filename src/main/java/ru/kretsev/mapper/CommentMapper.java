package ru.kretsev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.comment.Comment;

/**
 * Mapper interface for converting between Comment entities and DTOs.
 */
@Mapper(
        componentModel = "spring",
        uses = {TaskMapper.class, UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    /**
     * Converts a Comment DTO to a Comment entity.
     *
     * @param commentDto the comment DTO
     * @return the comment entity
     */
    @Mapping(source = "taskShortDto", target = "task")
    @Mapping(source = "userShortDto", target = "author")
    Comment toEntity(CommentDto commentDto);

    /**
     * Converts a Comment entity to a Comment DTO.
     *
     * @param comment the comment entity
     * @return the comment DTO
     */
    @Mapping(source = "task", target = "taskShortDto")
    @Mapping(source = "author", target = "userShortDto")
    CommentDto toDto(Comment comment);
}
