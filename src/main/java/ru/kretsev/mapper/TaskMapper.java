package ru.kretsev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.dto.task.TaskShortDto;
import ru.kretsev.model.task.Task;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "comments", source = "comments")
    Task toEntity(TaskDto taskDto);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "comments", source = "comments")
    TaskDto toDto(Task task);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    TaskShortDto toShortDto(Task task);
}
