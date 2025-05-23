package ru.kretsev.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.kretsev.service.EntityService;
import ru.kretsev.service.LoggingService;
import ru.kretsev.service.TaskService;

/**
 * Implementation of the TaskService for managing tasks.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final AuthenticationFacade authenticationFacade;
    private final EntityService entityService;
    private final LoggingService loggingService;

    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto, User user) {
        loggingService.logInfo("Попытка создания задачи: title={}, author={}", taskDto.title(), user.getEmail());

        Task task = taskMapper.toEntity(taskDto);
        task.setAuthor(user);
        task.setStatus(TaskStatus.PENDING);
        taskRepository.save(task);

        loggingService.logInfo("Задача успешно создана: id={}, title={}", task.getId(), task.getTitle());
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskDto assignTask(Long taskId, Long userId) {
        loggingService.logInfo("Попытка назначения задачи пользователю задачи: id={}, userId={}", taskId, userId);

        Task task = takeTask(taskId);
        User user = entityService.findEntityOrElseThrow(userRepository, userId, "Пользователь не найден");
        task.setAssignee(user);
        taskRepository.save(task);

        loggingService.logInfo(
                "Задача успешно назначена пользователю: id={}, title={}, assignee={}",
                task.getId(),
                task.getTitle(),
                user.getEmail());
        return taskMapper.toDto(task);
    }

    @Override
    public Page<TaskDto> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    @Override
    @Cacheable(value = "tasks", key = "#taskId")
    public TaskDto getTask(Long taskId) {
        loggingService.logInfo("Задача с id={} не найдена в кэше, выполняется запрос к базе данных", taskId);
        Task task = takeTask(taskId);

        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto> getUserTasks(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository.findByAuthor(user, pageable).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    @CachePut(value = "tasks", key = "#taskId")
    @Transactional
    public TaskDto updateTask(Long taskId, TaskDto taskDto, User user) {
        loggingService.logInfo("Обновление задачи: id={}, user={}, обновление кэша", taskId, user.getEmail());

        Task task = takeTask(taskId);

        if (!task.getAuthor().equals(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            loggingService.logWarn("Попытка обновления задачи без прав: taskId={}, user={}", taskId, user.getEmail());
            throw new AccessDeniedException("Нет прав для редактирования этой задачи");
        }

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(TaskStatus.valueOf(taskDto.status()));
        task.setPriority(Priority.valueOf(taskDto.priority()));
        taskRepository.save(task);

        loggingService.logInfo("Задача успешно обновлена: id={}, title={}", taskId, task.getTitle());
        return taskMapper.toDto(task);
    }

    @Override
    @CacheEvict(value = "tasks", key = "#taskId")
    @Transactional
    public void deleteTask(Long taskId) {
        loggingService.logInfo("Удаление задачи с id={} и удаление из кэша", taskId);

        Task task = takeTask(taskId);
        String currentUserEmail = authenticationFacade.getCurrentUserEmail();
        boolean isAdmin = authenticationFacade.getCurrentUserRoles().contains("ROLE_ADMIN");
        if (!isAdmin && !task.getAuthor().getEmail().equals(currentUserEmail)) {
            loggingService.logWarn("Попытка удаления задачи без прав: taskId={}, user={}", taskId, currentUserEmail);
            throw new AccessDeniedException("Вы можете удалить только свою задачу.");
        }

        taskRepository.deleteById(taskId);
    }

    @Override
    @CacheEvict(value = "comments", key = "#commentId")
    @Transactional
    public void deleteComment(Long taskId, Long commentId) {
        loggingService.logInfo("Удаление комментария с id={} и удаление из кэша", commentId);
        Comment comment = entityService.findEntityOrElseThrow(commentRepository, commentId, "Комментарий не найден");

        String currentUserEmail = authenticationFacade.getCurrentUserEmail();
        boolean isAdmin = authenticationFacade.getCurrentUserRoles().contains("ROLE_ADMIN");
        if (!isAdmin && !comment.getAuthor().getEmail().equals(currentUserEmail)) {
            loggingService.logWarn(
                    "Попытка удаления комментария без прав: commentId={}, user={}", commentId, currentUserEmail);
            throw new AccessDeniedException("Вы можете удалить только свои комментарии.");
        }

        if (!Objects.equals(comment.getTask().getId(), taskId)) {
            loggingService.logWarn(
                    "Попытка удаления комментария из другой задачи: taskId={}, commentId={}", taskId, commentId);
            throw new AccessDeniedException("Вы можете удалить комментарии только в выбранной задачи.");
        }

        commentRepository.delete(comment);
    }

    private Task takeTask(Long taskId) {
        return entityService.findEntityOrElseThrow(taskRepository, taskId, "Задача не найдена");
    }
}
