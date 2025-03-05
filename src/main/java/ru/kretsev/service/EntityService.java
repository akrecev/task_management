package ru.kretsev.service;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Service interface for common entity operations.
 */
public interface EntityService {
    /**
     * Finds an entity by ID or throws an exception if not found.
     *
     * @param repository the JPA repository
     * @param id the entity ID
     * @param errorMessage the error message to throw
     * @param <T> the entity type
     * @param <ID> the ID type
     * @return the found entity
     */
    @SuppressWarnings("java:S119")
    <T, ID> T findEntityOrElseThrow(JpaRepository<T, ID> repository, ID id, String errorMessage);
}
