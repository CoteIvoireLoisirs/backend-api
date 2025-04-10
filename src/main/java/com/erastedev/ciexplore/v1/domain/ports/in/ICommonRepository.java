package com.erastedev.ciexplore.v1.domain.ports.in;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 13:36:29
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-25 13:37:29
 */
@NoRepositoryBean
public interface ICommonRepository<T extends ICommonEntity<T>> extends JpaRepository<T, Long> {

    /**
     * Finds an entity by its ID, ensuring that it has not been deleted.
     *
     * @param id the ID of the entity to find.
     * @return an Optional containing the entity if found and not deleted, or empty if not found.
     */
    Optional<T> findByIdAndDeletedIsNull(Long id);

    /**
     * Finds an entity by its long ID, ensuring that it has not been deleted.
     *
     * @param id the long ID of the entity to find.
     * @return an Optional containing the entity if found and not deleted, or empty if not found.
     */
    Optional<T> findByIdAndDeletedIsNull(long id);

    /**
     * Retrieves all entities that have not been marked as deleted.
     *
     * @return a list of entities that are not marked as deleted.
     */
    List<T> findByDeletedIsNull();

    /**
     * Retrieves all entities that have not been marked as deleted.
     *
     * @return a list of entities that are not marked as deleted.
     */
    List<T> findAllByDeletedIsNull();
}
