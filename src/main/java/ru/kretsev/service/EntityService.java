package ru.kretsev.service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityService {
    @SuppressWarnings("java:S119")
    <T, ID> T findEntityOrElseThrow(JpaRepository<T, ID> repository, ID id, String errorMessage);
}
