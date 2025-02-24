package ru.kretsev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.kretsev.dto.comment.CommentDto;
import ru.kretsev.model.comment.Comment;

@Mapper(
        componentModel = "spring",
        uses = {TaskMapper.class, UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    @Mapping(source = "taskShortDto", target = "task")
    @Mapping(source = "userShortDto", target = "author")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "task", target = "taskShortDto")
    @Mapping(source = "author", target = "userShortDto")
    CommentDto toDto(Comment comment);
}
