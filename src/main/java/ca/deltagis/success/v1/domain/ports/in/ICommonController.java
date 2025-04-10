package ca.deltagis.success.v1.domain.ports.in;

import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 14:32:04
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-25 14:35:15
 */
public interface ICommonController<T extends ICommonEntity<T>> {
    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return ResponseEntity containing the entity data or an error message.
     */
    ResponseEntity<ApiResponse<T>> getById(String id);

    /**
     * Creates a new entity.
     *
     * @param entity the entity to create.
     * @return ResponseEntity containing the created entity or an error message.
     */
    ResponseEntity<ApiResponse<T>> create(T entity);

    /**
     * Updates an existing entity.
     *
     * @param id     The ID of the entity to update.
     * @param entity The updated entity data.
     * @return ResponseEntity containing the updated entity or an error message.
     */
    ResponseEntity<ApiResponse<T>> update(String id, T entity);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete.
     * @return ResponseEntity containing the result of the deletion operation.
     */
    ResponseEntity<ApiResponse<String>> delete(String id);

    /**
     * Provides access to the common service operations for the entity type.
     *
     * @return an instance of ICommonService for the specified entity type.
     */
    ICommonService<T> getService();

    /**
     * Returns the logger instance used by this controller.
     *
     * @return the logger used by this controller.
     */
    Logger getLogger();

    /**
     * Returns a list of default entities.
     *
     * @return a list of default entities.
     */
    List<T> getDefault();
}
