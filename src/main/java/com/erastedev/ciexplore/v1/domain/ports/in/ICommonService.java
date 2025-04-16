package com.erastedev.ciexplore.v1.domain.ports.in;


import com.erastedev.ciexplore.v1.domain.entities.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 13:47:11
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/ports/in/ICommonService.java
 * @Description: Service interface to manage common entity operations.
 */
public interface ICommonService<T extends ICommonEntity<T>> {

    /**
     * Retrieves the class of the entity.
     *
     * @return the class type of the entity.
     */
    Class<T> getClassName();

    /**
     * Retrieves an entity by its string ID.
     *
     * @param id the ID of the entity.
     * @return the entity with the given ID.
     */
    T getById(Long id);

    /**
     * Retrieves an entity by its string ID, wrapped in an Optional.
     *
     * @param id the ID of the entity.
     * @return an Optional containing the entity if found.
     */
    Optional<T> getByIdOptional(Long id);

    /**
     * Retrieves an entity that matches the given predicate.
     *
     * @param predicate the predicate to match.
     * @return the matching entity.
     */
    //T getByPredicate(Predicate predicate);

    /**
     * Retrieves all entities matching the given predicate and sorted by the specified sort.
     *.
     * @return a list of matching entities.
     * TODO : @Nullable Predicate predicate, @Nullable Sort sort
     */
    List<T> getAll();

    /**
     * Retrieves all entities from the repository.
     *
     * @return a list of all entities.
     */
    List<T> findAll();

    /**
     * Creates a new entity in the database.
     *
     * @param entity the entity to create.
     * @return the created entity.
     */
    T create(T entity);

    /**
     * Creates a new entity in the database, with the user who created it.
     *
     * @param entity the entity to create.
     * @param user   the user who creates the entity.
     * @return the created entity.
     */
    T create(T entity, User user);

    /**
     * Updates an existing entity.
     *
     * @param entity the entity to update.
     * @return the updated entity.
     */
    T update(T entity);

    /**
     * Updates an existing entity with the user who performed the update.
     *
     * @param entity the entity to update.
     * @param user   the user who updates the entity.
     * @return the updated entity.
     */
    T update(T entity, User user);

    /**
     * Deletes the given entity.
     *
     * @param entity the entity to delete.
     * @return a message indicating the result of the deletion.
     */
    String delete(T entity);

    /**
     * Deletes the given entity with the user who performed the deletion.
     *
     * @param entity the entity to delete.
     * @param user   the user who deletes the entity.
     * @return a message indicating the result of the deletion.
     */
    String delete(T entity, User user);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete.
     * @return a message indicating the result of the deletion.
     */
    Long deleteById(Long id);

    /**
     * Checks if an entity exists by its ID.
     *
     * @param id the ID to check.
     * @return true if an entity with the given ID exists, false otherwise.
     */
    boolean existsById(Long id);

    /**
     * Saves an entity in the database.
     *
     * @param entity the entity to save.
     * @return the saved entity.
     */
    T save(T entity);

    /**
     * Retrieves the repository associated with the service.
     *
     * @return the repository instance.
     */
    ICommonRepository<T> getRepository();


    

     
}
