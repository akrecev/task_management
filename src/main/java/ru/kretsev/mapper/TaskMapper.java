package ru.kretsev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.dto.task.TaskShortDto;
import ru.kretsev.model.task.Task;

/**
 * Mapper interface for converting between Task entities and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    /**
     * Converts a Task DTO to a Task entity.
     *
     * @param taskDto the task DTO
     * @return the task entity
     */
    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "comments", source = "comments")
    Task toEntity(TaskDto taskDto);

    /**
     * Converts a Task entity to a Task DTO.
     *
     * @param task the task entity
     * @return the task DTO
     */
    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "comments", source = "comments")
    TaskDto toDto(Task task);

    /**
     * Converts a Task entity to a short Task DTO.
     *
     * @param task the task entity
     * @return the short task DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    TaskShortDto toShortDto(Task task);
}
