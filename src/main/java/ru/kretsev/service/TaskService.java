package ru.kretsev.service;

import java.util.List;
import org.springframework.data.domain.Page;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.model.user.User;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto, User user);

    TaskDto assignTask(Long taskId, Long userId);

    Page<TaskDto> getAllTasks(int page, int size);

    TaskDto getTask(Long taskId);

    List<TaskDto> getUserTasks(User user);

    TaskDto updateTask(Long taskId, TaskDto taskDto, User user);

    void deleteTask(Long taskId);

    void deleteComment(Long commentId);
}
