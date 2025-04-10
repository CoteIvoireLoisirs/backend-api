package ca.deltagis.success.v1.adapters.web.http.exercice;

import java.util.HashMap;
import java.util.List;

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
import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.api.builder.ErrorDetailBuilder;
import ca.deltagis.success.v1.adapters.web.http.exercice.request.ExerciceSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.ProjectController;
import ca.deltagis.success.v1.application.services.exercice.ExerciceServiceImpl;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.billetage.Billetage;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import ca.deltagis.success.v1.domain.core.models.AuditLogActionCode;
import ca.deltagis.success.v1.domain.core.models.logs.Loggable;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@RestController
@RequestMapping(ApiEndpoints.EXERCICE)
@Tag(name = "Exercice API", description = "Operations related to the Right")
public class ExerciceController extends AbstractCommonController<Exercice> {

    @Getter
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private ExerciceServiceImpl service;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    @Autowired
    private LogServiceImpl<Exercice> logService;

    @Override
    public ICommonService<Exercice> getService() {
        return service;
    }

    @Override
    public List<Exercice> getDefault() {
        return getService().findAll();
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return ResponseEntity containing the workspace data or an error message.
     */

    @Override
    @Operation(summary = "Get exercice by ID", description = "Fetches a exercice using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercice retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "exercice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "Information exercice", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @GetMapping("/{id}")

    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Exercice>> getById(
            @Parameter(description = "ID of the exercise", required = true) @PathVariable String id) {
        try {
            Exercice exercice = service.getByIdExercice(Long.valueOf(id));

            return response.success("Exercice retrieved successfully", exercice, HttpStatus.OK);
        } catch (EntityNotFoundException e) {

            return response.error("Entity not found", e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {

            return response.error("Invalid ID format", e.getMessage(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new entity.
     *
     * @param exercice The entity data to be created.
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
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Exercice>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody Exercice exercice) {
        try {
            ExerciceSaveResponse entity = service.saveExercice(exercice);

            if (entity.getError() == null) {

                service.save(exercice);

                return response.success("Entity created successfully", entity.getExercice(), HttpStatus.CREATED);

            }
            return response.error("Failed to create exercice", entity.getError().name(), null,
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {

            HashMap<String, String> errors = new ErrorDetailBuilder().add("message", e.getMessage()).build();
            return response.error("Failed to create entity", e.getMessage(), errors, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing exercice.
     *
     * @param id      The ID of the exercice to update.
     * @param project The updated exercice data.
     * @return ResponseEntity containing the updated exercice or an error message.
     */

    @Operation(summary = "Update exercice", description = "Updates an existing exercice by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "exercice updated successfully"),
            @ApiResponse(responseCode = "404", description = "exercice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/{codeExercice}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Exercice>> updateExsercice(

            @Parameter(description = "Code of the exercice to be updated", required = true) @PathVariable String codeExercice,
            @Parameter(description = "Exercice data to be updated", required = true) @RequestBody Exercice exercice) {
        try {
            ExerciceSaveResponse entity = service.updateExercice(exercice, codeExercice);

            if (entity.getClass() != null) {
                return response.success("Exercice updated successfully", entity.getExercice(), HttpStatus.OK);
            }

            return response.error("Failed to update exercice", entity.getError().name(), null,
                    HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return response.internalError("Failed to update exercice", e);
        }
    }
  
   
}
