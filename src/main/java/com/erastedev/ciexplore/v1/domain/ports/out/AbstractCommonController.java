package com.erastedev.ciexplore.v1.domain.ports.out;


import com.erastedev.ciexplore.v1.adapters.web.api.service.ApiResponseService;
import com.erastedev.ciexplore.v1.application.services.logs.LogServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;
import com.erastedev.ciexplore.v1.domain.models.logs.Loggable;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonController;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract controller for handling common CRUD operations.
 *
 * @param <T> the type of the entity.
 */
// @Tag(name = "Common Entity API", description = "Operations related to common entities")
public abstract class AbstractCommonController<T extends ICommonEntity<T>> implements ICommonController<T> {
    private final Logger logger = LoggerFactory.getLogger(AbstractCommonController.class);

    private final HttpHeaders responseHeaders = new HttpHeaders();

    @Autowired
    private ApiResponseService apiResponseService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserAuthServiceImpl authService;

    @Autowired
    private LogServiceImpl<T> logService;

    // private final EnumSet<CrudOperation> allowedOperations;

    private final boolean enableErrorTrace = true;

    @Autowired
    public AbstractCommonController() {
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return ResponseEntity containing the entity data or an error message.
     */
    @Operation(summary = "Get Entity by ID", description = "Fetches an entity using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "User logged in", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @GetMapping("/{id}")
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<T>> getById(
            @Parameter(description = "ID of the entity", required = true) @PathVariable String id) {
        try {
            T entity = getService().getById(Long.valueOf(Long.valueOf(id).toString()));
            logger.info("Fetched entity with ID: {}", id);
            logService.logRead(entity, true);
            return apiResponseService.success("Entity retrieved successfully", entity, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Entity with ID {} not found", id);
            logService.logRead((T) null, false);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }
            return apiResponseService.error("Entity not found", e.getMessage(), errorDetails, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error fetching entity with ID: {}", id, e);
            logService.logRead((T) null, false);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }
            return apiResponseService.error("Internal Server Error", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all entities.
     *
     * @param allParams Optional query parameters for filtering entities.
     * @return ResponseEntity containing the list of entities or an error message.
     */
    @Operation(summary = "Get All Entities", description = "Fetches all entities, optionally filtered by query parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entities retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<List<T>>> getAlls(
            @Parameter(description = "Optional query parameters for filtering", required = false)
            @Nullable @RequestParam Map<String, String> allParams) {
        try {
            String filter = allParams.getOrDefault("filter", null);
            List<T> entities = getService().getAll();
            if (entities.isEmpty()) {
                logger.info("No entities found, returning default");
                entities = getDefault();
            }
            logService.logRead((T) null, true);
            return apiResponseService.success("Entities retrieved successfully", entities, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching entities", e);
            logService.logRead((T) null, false);

            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }

            return apiResponseService.error("Internal Server Error", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new entity.
     *
     * @param infoDto The entity data to be created.
     * @return ResponseEntity containing the created entity or an error message.
     */
    @Operation(summary = "Create Entity", description = "Creates a new entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to create entity"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<T>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody T infoDto
    ) {
        try {
            infoDto.setAutoFields();
            infoDto.setUpdateBy(authService.getCurrentLoggedUser().getId());
            T entity = getService().create(infoDto);
            logService.logCreate(entity, true);
            return apiResponseService.success("Entity created successfully", entity, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating entity", e);
            logService.logCreate((T) null, false);
            // return new ApiBuilder<T>().internalServerError("Failed to create entity", e);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
                errorDetails.put("cause", e.getCause() != null ? e.getCause().toString() : Arrays.stream(e.getStackTrace()).findFirst().get().toString());
                errorDetails.put("stackTrace", Arrays.stream(e.getStackTrace()).findFirst().get().toString());

            }
            return apiResponseService.internalError("Failed to create entity", e);
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param id      The ID of the entity to update.
     * @param infoDto The updated entity data.
     * @return ResponseEntity containing the updated entity or an error message.
     */
    @Operation(summary = "Update Entity", description = "Updates an existing entity by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<T>> update(
            @Parameter(description = "ID of the entity to update", required = true) @PathVariable String id,
            @Parameter(description = "Updated entity data", required = true) @RequestBody T infoDto) {
        try {
            T entity = getService().update(infoDto);
            logService.logUpdate(entity, true);
            infoDto.setUpdateBy(authService.getCurrentLoggedUser().getId());
            return apiResponseService.success("Entity updated successfully", entity, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Entity with ID {} not found for update", id);
            logService.logUpdate((T) null, false);

            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }
            return apiResponseService.error("Entity not found", e.getMessage(), errorDetails, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating entity with ID: {}", id, e);
            logService.logUpdate((T) null, false);

            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }
            return apiResponseService.error("Internal Server Error", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an entity by ID.
     *
     * @param id The ID of the entity to delete.
     * @return ResponseEntity containing the result of the deletion operation.
     */
    @Operation(summary = "Delete Entity", description = "Deletes an entity by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<String>> delete(
            @Parameter(description = "ID of the entity to delete", required = true) @PathVariable String id) {
        try {
            T entity = getService().getById(Long.valueOf(id));
            entity.setUpdateBy(authService.getCurrentLoggedUser().getId());
            String result = getService().delete(entity);
            logService.logDelete(entity, true, "Deleted " + entity.getClass().getSimpleName() + entity.getId());
            return apiResponseService.success("Entity deleted successfully", result, HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            logger.warn("Entity with ID {} not found for deletion", id);
            logService.logDelete((T) null, false, "Failed to delete " + e.getMessage());

            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }
            return apiResponseService.error("Entity not found", e.getMessage(), errorDetails, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting entity with ID: {}", id, e);
            logService.logDelete((T) null, false, "Failed to delete " + e.getMessage());
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            if (enableErrorTrace) {
                errorDetails.put("errorTrace", e.getMessage());
            }
            return apiResponseService.error("Internal Server Error", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
