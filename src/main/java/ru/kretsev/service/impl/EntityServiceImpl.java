package ru.kretsev.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.kretsev.service.EntityService;
import ru.kretsev.service.LoggingService;

/**
 * Implementation of the EntityService for common entity operations.
 */
@Service
@RequiredArgsConstructor
public class EntityServiceImpl implements EntityService {
    private final LoggingService loggingService;

    @Override
    @SuppressWarnings("java:S119")
    public <T, ID> T findEntityOrElseThrow(JpaRepository<T, ID> repository, ID id, String errorMessage) {
        return repository.findById(id).orElseThrow(() -> {
            loggingService.logError("{}: id={}", errorMessage, id);
            return new EntityNotFoundException(errorMessage);
        });
    }
}
