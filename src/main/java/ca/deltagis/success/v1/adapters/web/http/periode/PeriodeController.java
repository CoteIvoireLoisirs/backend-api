package ca.deltagis.success.v1.adapters.web.http.periode;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ca.deltagis.success.v1.adapters.web.http.periode.request.PeriodeSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.ProjectController;
import ca.deltagis.success.v1.adapters.web.message.PeriodeMessage;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.application.services.periode.PeriodeServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.periode.Periode;
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
@RequestMapping(ApiEndpoints.PERIODE)
@Tag(name = "periode API", description = "Operations related to the Right")
public class PeriodeController extends AbstractCommonController<Periode> {

    
    @Getter
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private PeriodeServiceImpl service;
    @Autowired
    private UserAuthServiceImpl userAuthService;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
     private LogServiceImpl logService;

     
    @Autowired
    private ApiResponseService response;

     public Class<?> className() {
        return this.getClass();
    }

    
    @Override
    public ICommonService<Periode> getService() {
        return service;
    }

    
    @Override
    public List<Periode> getDefault() {
        return getService().findAll();
    }



    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return ResponseEntity containing the workspace data or an error message.
     */


     @Override
     @Operation(summary = "Get periode by ID", description = "Fetches a periode using its ID.")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Periode retrieved successfully"),
             @ApiResponse(responseCode = "400", description = "Invalid ID format"),
             @ApiResponse(responseCode = "404", description = "Periode not found"),
             @ApiResponse(responseCode = "500", description = "Internal Server Error")
     })
     @Loggable(
             action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, 
             message = "Information periode", 
             actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED
     )
     @GetMapping("/{id}")
      public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Periode>> getById(
             @Parameter(description = "ID of the periode", required = true) 
             @PathVariable String id) {
     
         try {
          
             if (id == null || id.trim().isEmpty()) {
                 return response.error("Invalid ID format", "ID cannot be null or empty", null, HttpStatus.BAD_REQUEST);
             }
     
             Long periodeId;
             try {
                 periodeId = Long.valueOf(id);
             } catch (NumberFormatException e) {
                 return response.error("Invalid ID format", "ID must be a numeric value", null, HttpStatus.BAD_REQUEST);
             }
     
             
             Periode periode = service.getByIdPeriode(periodeId);
             return response.success("Periode retrieved successfully", periode, HttpStatus.OK);
     
         } catch (EntityNotFoundException e) {
             return response.error("Entity not found", e.getMessage(), null, HttpStatus.NOT_FOUND);
         } catch (Exception e) {
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
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Periode>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody Periode periode) {
        try {
           PeriodeSaveResponse  entity = service.savePeriode(periode);

            if (Objects.requireNonNull(entity.getError()) == PeriodeMessage.ALREADY_EXISTS) {
                return response.error(entity.getError().name(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
            }

            service.save(periode);

            return response.success("Entity created successfully", entity.getPeriode(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating entity", e);
            HashMap<String, String> errors = new ErrorDetailBuilder().add("message", e.getMessage()).build();
            return response.error("Failed to create entity", e.getMessage(), errors, HttpStatus.BAD_REQUEST);
        }
    }




    /***
 * this function allow you to modify the workspace and
 * associate an image with it
 * @param periode is the periode of the user to modify
 * @param id is the id of the workspace
 */
    @Override
    @Operation(summary = "Update Periode", description = "Updates an existing workspace by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Periode updated successfully"),
            @ApiResponse(responseCode = "404", description = "Periode not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Periode>> update(
            @Parameter(description = "ID of the entity to update", required = true) @PathVariable String id,
            @Parameter(description = "Updated entity data", required = true) @RequestBody Periode periode) {
        try {
         PeriodeSaveResponse entity = service.updatePeriode(periode);
            if (entity != null) {
                return response.success("Entity updated successfully",entity.getPeriode(), HttpStatus.OK);
            } else {
                return response.error("Periode not found", "No Workspace with the specified ID found", null, HttpStatus.NOT_FOUND);
         
            }
        } catch (Exception e) {
            logger.error("Error updating entity with ID: {}", id, e);
            logService.logDelete(null, false, "Failed to update: " + e.getMessage());
            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     
                 }
             




             





