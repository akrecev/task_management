package ru.kretsev.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.kretsev.service.EntityService;

/**
 * Implementation of the EntityService for common entity operations.
 */
@Slf4j
@Service
public class EntityServiceImpl implements EntityService {
    @Override
    @SuppressWarnings("java:S119")
    public <T, ID> T findEntityOrElseThrow(JpaRepository<T, ID> repository, ID id, String errorMessage) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("{}: id={}", errorMessage, id);
            return new EntityNotFoundException(errorMessage);
        });
    }
}
