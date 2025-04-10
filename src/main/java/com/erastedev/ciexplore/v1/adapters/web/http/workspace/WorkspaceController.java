/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-27 13:11:03
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-28 11:26:42
 */
package com.erastedev.ciexplore.v1.adapters.web.http.workspace;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erastedev.ciexplore.v1.adapters.web.api.endpoints.ApiEndpoints;
import com.erastedev.ciexplore.v1.adapters.web.api.builder.ApiBuilder;
import com.erastedev.ciexplore.v1.adapters.web.api.service.ApiResponseService;
import com.erastedev.ciexplore.v1.adapters.web.api.builder.ErrorDetailBuilder;
import com.erastedev.ciexplore.v1.application.request.workspace.WorkspaceSaveResponse;
import com.erastedev.ciexplore.v1.adapters.web.message.WorkspaceMessage;
import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import com.erastedev.ciexplore.v1.application.services.files.FileStorageServiceImpl;
import com.erastedev.ciexplore.v1.application.services.logs.LogServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.application.services.workspace.WorkspaceServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.workspace.WorkSpaceStatus;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.models.logs.Loggable;
import com.erastedev.ciexplore.v1.domain.models.workspace.WorkspacePublic;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonService;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@RestController
@RequestMapping(ApiEndpoints.WORKSPACES)
@Tag(name = "Workspace API", description = "Operations related to the Right")
public class WorkspaceController extends AbstractCommonController<Workspace> {
    @Autowired
    private WorkspaceServiceImpl service;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Getter
    private Logger logger = LoggerFactory.getLogger(WorkspaceController.class);

    @Autowired
    private LogServiceImpl<Workspace> logService;

    @Autowired
    FileStorageServiceImpl storageService;

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    public WorkspaceController(
            ApiResponseService apiResponseService, UserServiceImpl userServiceImpl, LogServiceImpl logService) {
    }

    @Override
    public ICommonService<Workspace> getService() {
        return service;
    }

    @Override
    public List<Workspace> getDefault() {
        return getService().findAll();
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return ResponseEntity containing the workspace data or an error message.
     */
    @Override
    @Operation(summary = "Get workspace by ID", description = "Fetches a workspace using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "Information workspace", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @GetMapping("/{id}")
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<Workspace>> getById(
            @Parameter(description = "ID of the workspace", required = true) @PathVariable String id) {
        try {
            Workspace workspace = service.getByIdWorkspace(Long.valueOf(id));
            if (workspace == null) {
                return response.error("Workspace not found", "No Workspace with the specified ID found", null, HttpStatus.NOT_FOUND);
            }
            workspace.setImageUrl(storageService.getUrlFile(workspace.getImagePath()));
            return response.success("Workspace retrieved successfully", workspace, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching entity with ID: {}", id, e);
            logService.logRead((Workspace) null, false);
            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new entity.
     *
     * @param workspace The entity data to be created.
     * @return ResponseEntity containing the created entity or an error message.
     */
    @Override
    @Operation(summary = "Create Entity", description = "Creates a new entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to create entity"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<Workspace>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody Workspace workspace) {
        try {
            WorkspaceSaveResponse entity = service.saveWorkspace(workspace);

            if (Objects.requireNonNull(entity.getError()) == WorkspaceMessage.ALREADY_EXISTS) {
                return response.error(entity.getError().name(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
            }

            service.save(workspace);

            return response.success("Entity created successfully", entity.getWorkspace(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating entity", e);
            HashMap<String, String> errors = new ErrorDetailBuilder().add("message", e.getMessage()).build();
            return response.error("Failed to create entity", e.getMessage(), errors, HttpStatus.BAD_REQUEST);
        }
    }

    /***
     * this function allow you to modify the workspace and
     * associate an image with it
     * @param workspace is the workspace of the user to modify
     * @param id is the id of the workspace
     */
    @Override
    @Operation(summary = "Update workspace", description = "Updates an existing workspace by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace updated successfully"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<Workspace>> update(
            @Parameter(description = "ID of the entity to update", required = true) @PathVariable String id,
            @Parameter(description = "Updated entity data", required = true) @RequestBody Workspace workspace) {
        try {
            WorkspaceSaveResponse entity = service.updateWorkspace(workspace);
            if (entity != null) {
                return response.success("Entity updated successfully", entity.getWorkspace(), HttpStatus.OK);
            } else {
                return response.error("Workspace not found", "No Workspace with the specified ID found", null, HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            logger.error("Error updating entity with ID: {}", id, e);
            logService.logDelete(null, false, "Failed to update: " + e.getMessage());
            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Uploads an image and updates the workspace image.
     * <p>
     * This method uploads an image from the given file and sets it as the workspace's profile image.
     * The image is stored in the workspace's directory in the storage specified in the application
     * configuration. The image is stored with the same name as the original file. If the image is
     * uploaded successfully, a response with a 200 status code is returned. If the image is not
     * uploaded successfully, appropriate error responses with status codes 400 or 500 are returned,
     * respectively. In case of an internal server error, a 500 status code is returned.
     * <p>
     * The response contains a json object with the following fields:
     * <ul>
     * <li>fileName - the name of the uploaded file</li>
     * <li>errorType - the type of error if the image was not uploaded successfully</li>
     * <li>message - a message describing the result of the upload</li>
     * </ul>
     *
     * @param file   the image to upload
     * @param code   the code of the workspace to update
     * @return a ResponseEntity containing the result of the upload operation
     */
    @Operation(summary = "Upload Image", description = "Uploads an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(ApiEndpoints.WORKSPACE_CHANGE_IMAGE)
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<FileUploadResponse>> uploadImage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody() MultipartFile file,
            @PathVariable String code) {
        try {
            WorkspaceSaveResponse entity = service.updateWorkspaceImage(file, code);
            FileUploadError errorType = entity.getError() != null
                    ? FileUploadError.fromFileUploadError(entity.getError())
                    : null;
            String message = (errorType == null || entity.getError() == WorkspaceMessage.NONE)
                    ? "Image uploaded successfully"
                    : entity.getError().name();

            return new ApiBuilder<FileUploadResponse>()
                    .withMessage(message)
                    .withData(new FileUploadResponse(file.getOriginalFilename(), errorType, message))
                    .withSuccess(errorType == null)
                    .withStatus(errorType == null ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                    .buildWithResponseEntity();
        } catch (RuntimeException e) {
            return new ApiBuilder<FileUploadResponse>().internalServerError("Failed to upload image", e);
        }
    }

    /**
     * Deletes a workspace by its ID.
     *
     * <p>
     * This method deletes a workspace and returns a response indicating the result
     * of the operation.
     * If the workspace is not found, an error response is returned.
     *
     * @param id The ID of the workspace to be deleted.
     * @return ResponseEntity containing the result of the deletion or an error
     * message.
     */
    @Override
    @Operation(summary = "Delete workspace", description = "Deletes an existing entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<String>> delete(
            @Parameter(description = "ID of the entity to delete", required = true) @PathVariable String id) {
        try {

            Workspace entity = getService().getById(Long.valueOf(id));
            if (entity == null) {
                throw new EntityNotFoundException("Workspace with ID " + id + " not found");
            }

            entity.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
            entity.setStatus(WorkSpaceStatus.DELETED);

            String result = getService().delete(entity);

            logService.logDelete(entity, true,
                    "Deleted " + entity.getClass().getSimpleName() + " with ID " + entity.getId());

            return response.success("Entity deleted successfully", result, HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {

            logger.warn("Entity with ID {} not found for deletion", id);
            logService.logDelete(null, false, "Failed to delete: " + e.getMessage());
            return response.error("Entity not found", e.getMessage(), null, HttpStatus.NOT_FOUND);

        } catch (Exception e) {

            logger.error("Error deleting entity with ID: {}", id, e);
            logService.logDelete(null, false, "Failed to delete: " + e.getMessage());
            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Activates a workspace by its code.
     *
     * <p>
     * This method updates the status of a workspace to active and returns the
     * updated workspace
     * in a successful ApiResponse. In case of an exception or if the workspace is
     * already active, an error response is returned.
     *
     * @param code the code of the workspace to be activated.
     * @return ResponseEntity containing the updated workspace or an error message.
     */
    @Operation(summary = "Update workspace status", description = "Updates an existing entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(ApiEndpoints.ACTIVATED_WORKSPACE)
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<Workspace>> activate(
            @Parameter(description = "code of the workspace to be activated", required = true) @PathVariable String code) {

        try {
            WorkspaceSaveResponse Entity = service.activeWorkspace(code);
            if (Entity.getError() == WorkspaceMessage.ALREADY_PENDING) {
                return response.error("Workspace is already active", Entity.getError().name(), null,
                        HttpStatus.BAD_REQUEST);
            } else {
                return response.success("Workspace is activated", HttpStatus.CREATED);

            }
        } catch (Exception e) {
            logger.error("Error activating workspace", e);
            return response.error("Failed to activate workspace", e.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * Disables a workspace by its code.
     *
     * <p>
     * This method updates the status of a workspace to disabled and returns the
     * updated workspace
     * in a successful ApiResponse. In case of an exception or if the workspace is
     * already disabled, an error response is returned.
     *
     * @param code the code of the workspace to be disabled.
     * @return ResponseEntity containing the updated workspace or an error message.
     */
    @Operation(summary = "Disable workspace ", description = "Disable an existing entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(ApiEndpoints.DISABLE_WORKSPACE)
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<Workspace>> disable(
            @Parameter(description = "code of the workspace to be Disable", required = true) @PathVariable String code) {

        try {
            WorkspaceSaveResponse Entity = service.disableWorkspace(code);
            if (Entity.getError() == WorkspaceMessage.ERROR_STATUS) {
                return response.error("deactivation of workspace impossible", Entity.getError().name(), null,
                        HttpStatus.BAD_REQUEST);
            } else {
                return response.success("workspace has been deactivated", HttpStatus.CREATED);

            }
        } catch (Exception e) {
            logger.error("Error activating workspace", e);
            return response.error("Failed to disable workspace", e.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * Retrieves a list of all workspaces without requiring authorization.
     *
     * <p>
     * This method fetches all workspaces and returns them in a successful
     * ApiResponse.
     * In case of an exception, an error response is returned.
     *
     * @return ResponseEntity containing the list of WorkspacePublic objects
     * or an error message.
     */
    @Operation(summary = "Get all workspace", description = "Get all workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(ApiEndpoints.PUBLIC_WORKSPACE)
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<List<WorkspacePublic>>> getAllWithOutAuthorization() {
        try {
            List<WorkspacePublic> entities = service.getWorkspaceWithOutAuthorization();
            return response.success("Entity found successfully", entities, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error activating workspace", e);
            return response.error("Failed to disable workspace", e.getMessage(), null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
