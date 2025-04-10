package ca.deltagis.success.v1.adapters.web.http.billetage;

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
import ca.deltagis.success.v1.adapters.web.http.billetage.request.BilletageSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.ProjectController;
import ca.deltagis.success.v1.application.services.billetage.BilletageServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.billetage.Billetage;
import ca.deltagis.success.v1.domain.core.models.AuditLogActionCode;
import ca.deltagis.success.v1.domain.core.models.logs.Loggable;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@RestController
@RequestMapping(ApiEndpoints.BILLETAGE)
@Tag(name = "Billetage API", description = "Operations related to the Right")
public class BilletageController {

    @Getter
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ApiResponseService response;

    @Autowired
    private BilletageServiceImpl billetageServiceImpl;

    public Class<?> className() {
        return this.getClass();
    }

    public ICommonService<Billetage> getService() {
        return billetageServiceImpl;
    }

    @Operation(summary = "Get Billetage by ID", description = "Fetches a Billetage using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercice retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "exercice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "Information billetage", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @GetMapping("/{id}")

    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Billetage>> getById(
            @Parameter(description = "ID of the billatage", required = true) @PathVariable Long id) {
        try {
            Billetage billetage = billetageServiceImpl.getByIdBilletage(id);

            return response.success("billetage retrieved successfully", billetage, HttpStatus.OK);
        } catch (EntityNotFoundException e) {

            return response.error("Entity not found", e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {

            return response.error("Invalid ID format", e.getMessage(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Billetage by ID", description = "Fetches a Billetage using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercice retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "exercice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "Information billetage", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @GetMapping

    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<List<Billetage>>> getByAll() {
        try {
            List<Billetage> billetage = billetageServiceImpl.getAll();

            return response.success("billetage retrieved successfully", billetage, HttpStatus.OK);
        } catch (EntityNotFoundException e) {

            return response.error("Entity not found", e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {

            return response.error("Invalid ID format", e.getMessage(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "delate Billetage by ID", description = "Fetches a Billetage using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Billetage retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Billetage not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "Information billetage", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @DeleteMapping("/{id}")

    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<List<Billetage>>> delateById(
            @Parameter(description = "ID of the billatage", required = true) @PathVariable Long id) {
        try {
            Long deletedId = billetageServiceImpl.deleteById(id);
            if (deletedId == null) {
                return response.error("Billetage not found", "No Billetage found with the provided ID", null,
                        HttpStatus.NOT_FOUND);
            }

            return response.success("billetage retrieved successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return response.error("Invalid ID format", e.getMessage(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Billetage by currencyCode", description = "Fetches a billetage using its currencyCode.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "billetage retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "billetage not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @PostMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Billetage>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody Billetage billetage) {
        try {

            BilletageSaveResponse entityResponse = billetageServiceImpl.saveBilletage(billetage);

            if (entityResponse.getError() != null) {
                return response.error(
                        "Failed to create billetage",
                        entityResponse.getError().name(),
                        null,
                        HttpStatus.BAD_REQUEST);
            }

            return response.success(
                    "Entity created successfully",
                    entityResponse.getBilletage(),
                    HttpStatus.CREATED);
        } catch (Exception e) {

            HashMap<String, String> errors = new ErrorDetailBuilder()
                    .add("message", e.getMessage())
                    .build();
            return response.error("Failed to create entity", e.getMessage(), errors, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Billetage>> update(
            @RequestBody Billetage billetage, @PathVariable Long id) {

        try {

            BilletageSaveResponse entity = billetageServiceImpl.updateBilletage(billetage, id);
            if (entity.getErrorDetail() == null) {
                return response.success("Billetage updated successfully", entity.getBilletage(), HttpStatus.OK);
            }
            return response.error("Failed to update billetage", entity.getError().name(), null,
                    HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return response.internalError("Failed to update billetage", e);
        }
    }

}
