/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-08 14:06:40
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-08 14:06:43
 */
package ca.deltagis.success.v1.adapters.web.http.rights;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.message.rights.RightCustomMessage;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.application.services.rights.RightServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.rights.Right;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.RIGHTS)
@Tag(name = "Right API", description = "Operations related to the Workspace")
//  extends AbstractCommonController<Right>
public class RightController {
    @Autowired
    private RightServiceImpl service;

    @Getter
    private Logger logger = LoggerFactory.getLogger(RightController.class);

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    // @Override
    public ICommonService<Right> getService() {
        return service;
    }

    // @Override
    public List<Right> getDefault() {
        return getService().findAll();
    }

    /**
     * Updates an existing entity by ID. But system rights cannot be updated.
     *
     * @param id      The ID of the entity to update.
     * @param infoDto The updated entity data.
     * @return ResponseEntity containing the updated entity or an error message.
     */
    // @Override
    @Operation(summary = "Update Entity", description = "Updates an existing entity by ID. But system rights cannot be updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Right>> update(
            @Parameter(description = "ID of the entity to update", required = true) @PathVariable String id,
            @Parameter(description = "Updated entity data", required = true) @RequestBody Right infoDto) {
        try {
            if (service.isSystemRight(infoDto)) {
                return response.error("Failed to update entity", RightCustomMessage.CANNOT_UPDATE_SYSTEM_RIGHT.getMessage(), null, HttpStatus.BAD_REQUEST);
            }
            Right entity = getService().update(infoDto);
            return response.success("Entity updated successfully", entity, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Entity with ID {} not found for update", id);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            return response.error("Entity not found", e.getMessage(), errorDetails, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating entity with ID: {}", id, e);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            return response.error("Internal Server Error", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an entity by its ID. System rights cannot be deleted.
     *
     * @param id the ID of the entity to delete.
     * @return ResponseEntity containing the result of the deletion operation.
     */
    // @Override
    @Operation(summary = "Delete Entity", description = "Deletes an entity by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<String>> delete(
            @Parameter(description = "ID of the entity to delete", required = true) @PathVariable String id) {
        try {
            Right entity = getService().getById(Long.valueOf(id));
            if (service.isSystemRight(entity)) {
                return response.error("Failed to update entity", RightCustomMessage.CANNOT_UPDATE_SYSTEM_RIGHT.getMessage(), null, HttpStatus.BAD_REQUEST);
            }
            String result = getService().delete(entity);
            return response.success("Entity deleted successfully", result, HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            logger.warn("Entity with ID {} not found for deletion", id);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            return response.error("Entity not found", e.getMessage(), errorDetails, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting entity with ID: {}", id, e);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            return response.error("Internal Server Error", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
