package ru.kretsev.dto.task;

/**
 * Short Data Transfer Object for a task.
 *
 * @param id the task ID
 * @param title the task title
 */
public record TaskShortDto(Long id, String title) {}
