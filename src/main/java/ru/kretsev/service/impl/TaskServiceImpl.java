package ru.kretsev.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.kretsev.auth.AuthenticationFacade;
import ru.kretsev.dto.task.TaskDto;
import ru.kretsev.mapper.TaskMapper;
import ru.kretsev.model.comment.Comment;
import ru.kretsev.model.task.Priority;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.task.TaskStatus;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.CommentRepository;
import ru.kretsev.repository.TaskRepository;
import ru.kretsev.repository.UserRepository;
import ru.kretsev.service.TaskService;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public TaskDto createTask(TaskDto taskDto, User user) {
        Task task = taskMapper.toEntity(taskDto);
        task.setAuthor(user);
        task.setStatus(TaskStatus.PENDING);
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(throwTaskNotFound());

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        task.setAssignee(user);
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Page<TaskDto> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    @Override
    public TaskDto getTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(throwTaskNotFound());
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto> getUserTasks(User user) {
        return taskRepository.findByAuthor(user).stream().map(taskMapper::toDto).toList();
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskDto taskDto, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(throwTaskNotFound());

        if (!task.getAuthor().equals(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Нет прав для редактирования этой задачи");
        }

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(TaskStatus.valueOf(taskDto.status()));
        task.setPriority(Priority.valueOf(taskDto.priority()));
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(throwTaskNotFound());

        String currentUserEmail = authenticationFacade.getCurrentUserEmail();
        boolean isAdmin = authenticationFacade.getCurrentUserRoles().contains("ROLE_ADMIN");
        if (!isAdmin && !task.getAuthor().getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException("Вы можете удалить только свою задачу.");
        }

        taskRepository.deleteById(taskId);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(throwTaskNotFound());

        String currentUserEmail = authenticationFacade.getCurrentUserEmail();
        boolean isAdmin = authenticationFacade.getCurrentUserRoles().contains("ROLE_ADMIN");
        if (!isAdmin && !comment.getAuthor().getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException("Вы можете удалить только свои комментарии.");
        }

        commentRepository.delete(comment);
    }

    private static Supplier<EntityNotFoundException> throwTaskNotFound() {
        return () -> new EntityNotFoundException("Задача не найдена");
    }
}
