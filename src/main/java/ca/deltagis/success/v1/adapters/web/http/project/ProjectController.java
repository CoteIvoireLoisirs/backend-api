package ca.deltagis.success.v1.adapters.web.http.project;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.builder.ApiBuilder;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectsByWorkspaceResponse;
import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.application.services.project.ProjectServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.core.models.FileUploadResponse;
import ca.deltagis.success.v1.domain.core.models.project.ProjectStatus;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.PROJECTS)
@Tag(name = "Project API", description = "Operations related to the Right")
public class ProjectController extends AbstractCommonController<Project> {

    @Getter
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectServiceImpl service;

    @Autowired
    private ApiResponseService response;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private LogServiceImpl logService;

    public Class<?> className() {
        return this.getClass();
    }

    @Override
    public ICommonService<Project> getService() {
        return service;
    }

    @Override
    public List<Project> getDefault() {
        return getService().findAll();
    }

    /**
     * Retrieves all projects from a specified workspace.
     *
     * @param workspaceCode The code of the workspace.
     * @return A list of projects that belong to the specified workspace.
     */
    @Operation(summary = "Get Projects From Workspace", description = "Retrieves all projects from a specified workspace.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to retrieve projects"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(ApiEndpoints.PROJECT_FROM_WORKSPACE)
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<List<Project>>> getProjectsFromWorkspace(
            @Parameter @PathVariable String workspaceCode) {
        try {
            ProjectsByWorkspaceResponse fromWorkspace = service.getProjectsFromWorkspace(workspaceCode);
            if (fromWorkspace.getError() != null) {
                return response.error("Failed to retrieve projects", fromWorkspace.getError().name(), null,
                        HttpStatus.BAD_REQUEST);
            }
            return response.success("Projects retrieved successfully", fromWorkspace.getProjects(), HttpStatus.OK);
        } catch (Exception e) {
            return response.internalError("Failed to retrieve projects", e);
        }
    }

    /**
     * Creates a new entity.
     *
     * @param project The entity data to be created.
     * @return The response with the details of the saved project and a status
     *         message.
     */
    @Override
    @Operation(summary = "Create Entity", description = "Creates a new entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to create entity"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Project>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody Project project) {
        try {
            ProjectSaveResponse entity = service.saveProject(project);
            if (entity.getError() != null) {
                return response.error("Failed to create project", entity.getError().name(), null,
                        HttpStatus.BAD_REQUEST);
            }

            return response.success("Entity created successfully", entity.getProject(), HttpStatus.CREATED);
        } catch (Exception e) {
            return response.internalError("Failed to create entity", e);
        }
    }

    /**
     * Updates an existing project.
     *
     * @param id      The ID of the project to update.
     * @param project The updated project data.
     * @return ResponseEntity containing the updated project or an error message.
     */
    @Override
    @Operation(summary = "Update project", description = "Updates an existing project by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "project updated successfully"),
            @ApiResponse(responseCode = "404", description = "project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Project>> update(
            @Parameter(description = "ID of the project to be updated", required = true) @PathVariable String id,
            @Parameter(description = "Project data to be updated", required = true) @RequestBody Project project) {
        try {
            ProjectSaveResponse entity = service.updateProject(project, Long.valueOf(id));
            if (entity.getError() != null) {
                return response.error("Failed to update project", entity.getError().name(), null,
                        HttpStatus.BAD_REQUEST);
            }
            return response.success("Project updated successfully", entity.getProject(), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return response.internalError("Failed to update project", e);
        }
    }

    /**
     * Close a project.
     *
     * @param id The project to be closed.
     * @return The response with the details of the closed project and a status
     *         message.
     */
    @Operation(summary = "Close Project", description = "Closes a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project closed successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to close project"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(ApiEndpoints.CLOSE_PROJECT)
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Project>> closeProject(
            @RequestParam int id) {
        try {
            ProjectSaveResponse entity = service.closeProject(id);
            if (entity.getError() != null) {
                return response.error(entity.getError().name(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
            }

            return response.success("Project closed successfully", entity.getProject(), HttpStatus.OK);
        } catch (Exception e) {
            return response.internalError("Failed to close project", e);
        }
    }

    /**
     * Archive a project.
     *
     * @param id The project to be archived.
     * @return The response with the details of the archived project and a status
     *         message.
     */
    @Operation(summary = "Archive Project", description = "Archives a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project archived successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to archive project"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(ApiEndpoints.ARCHIVE_PROJECT)
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Project>> archiveProject(
            @RequestParam int id) {
        try {
            ProjectSaveResponse entity = service.archiveProject(id);
            if (entity.getError() != null) {
                return response.error(entity.getError().name(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
            }

            return response.success("Project archived successfully", entity.getProject(), HttpStatus.OK);
        } catch (Exception e) {
            return response.internalError("Failed to archive project", e);
        }
    }

    @Operation(summary = "Upload Image", description = "Uploads an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(ApiEndpoints.PROJECT_CHANGE_IMAGE)
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<FileUploadResponse>> uploadImage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody() MultipartFile file,
            @PathVariable String code) {
        try {
            ProjectSaveResponse entity = service.updateProjectImage(file, code);
            FileUploadError errorType = entity.getError() != null
                    ? FileUploadError.fromFileUploadError(entity.getError())
                    : null;
            String message = (errorType == null || entity.getError() == ProjectMessage.NONE)
                    ? "Image uploaded successfully"
                    : entity.getError().name();

            return new ApiBuilder<FileUploadResponse>()
                    .withMessage(message)
                    .withData(new FileUploadResponse(file.getOriginalFilename(), errorType, message))
                    .withSuccess(errorType == null)
                    .withStatus(errorType == null ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                    .buildWithResponseEntity();
        } catch (RuntimeException e) {
            FileUploadResponse errorResponse = new FileUploadResponse(file.getOriginalFilename(),
                    FileUploadError.INVALID_FILE_TYPE, e.getMessage());
            return new ApiBuilder<FileUploadResponse>()
                    .withMessage(e.getMessage())
                    .withData(errorResponse)
                    .withSuccess(false)
                    .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .buildWithResponseEntity();
        }
    }

    @Override
    @Operation(summary = "Delete project", description = "Deletes an existing entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<String>> delete(
            @Parameter(description = "ID of the entity to delete", required = true) @PathVariable String id) {
        try {

            Long entityId;
            try {
                entityId = Long.valueOf(id);
            } catch (NumberFormatException e) {
                logger.warn("Invalid ID format: {}", id);
                return response.error("Invalid ID format", "The provided ID is not a valid number", null,
                        HttpStatus.BAD_REQUEST);
            }

            Project entity = getService().getById(entityId);
            if (entity == null) {
                throw new EntityNotFoundException("Project with ID " + id + " not found");
            }

            if (entity.getStatus() == ProjectStatus.DELETED) {
                logger.warn("Entity with ID {} is already deleted", id);
                return response.error("Entity already deleted", "Project is already marked as deleted", null,
                        HttpStatus.CONFLICT);
            }

            entity.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
            entity.setStatus(ProjectStatus.DELETED);

            String result = getService().delete(entity);

            logService.logDelete(entity, true,
                    "Deleted " + entity.getClass().getSimpleName() + " with ID " + entity.getId());

            return response.success("Entity deleted successfully", result, HttpStatus.OK);

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

}
